drop function if exists escape_label;

delimiter $$
create function escape_label(label text) returns text
begin
	return replace(replace(label, '>', '&gt;'), '<', '&lt;');
end $$
delimiter ;

drop function if exists class_name;

delimiter $$
create function class_name(qualified_class_name text) returns text
begin
	return substring_index(qualified_class_name, '.', -1);
end $$
delimiter ;

drop function if exists export_node;

delimiter $$
create function export_node(id text, label text, color text) returns text
begin
	declare template text default '{id} [label={label}, color={color}]';
	return replace(
		replace(
			replace(
				template,
				'{id}',
				id
			),
			'{label}',
			label
		),
		'{color}',
		color
	);
end $$
delimiter ;

drop function if exists export_edge;

delimiter $$
create function export_edge(source text, target text, label text) returns text
begin
	declare template text default '{source} -> {target} [label={label}]';
	return replace(
		replace(
			replace(
				template,
				'{source}',
				source
			),
			'{target}',
			target
		),
		'{label}',
		label
	);
end $$
delimiter ;

drop function if exists export_subgraph;

delimiter $$
create function export_subgraph(node text, edge text, subgraph text) returns text
begin
	declare template text default '{node}
	{edge}
	{subgraph}';
	return replace(
		replace(
			replace(
				template,
				'{node}',
				node
			),
			'{edge}',
			edge
		),
		'{subgraph}',
		subgraph
	);
end $$
delimiter ;

drop function if exists export_compilation_unit_graph;

delimiter $$
create function export_compilation_unit_graph(compilation_unit_id bigint(20)) returns text
begin
	declare template text default 'digraph {
	node [shape=rectangle, style=filled]
	{compilation_unit_subgraph}
}';
	set group_concat_max_len = 18446744073709551615;
	return replace(
		template,
		'{compilation_unit_subgraph}',
		export_compilation_unit_subgraph(compilation_unit_id)
	);
end $$
delimiter ;

drop function if exists export_compilation_unit_subgraph;

delimiter $$
create function export_compilation_unit_subgraph(compilation_unit_id bigint(20)) returns text
begin
	declare compilation_unit_node_id text;
	declare node_subgraph text;
	select concat('CompilationUnit', compilation_unit_id)
		into compilation_unit_node_id;
	select group_concat(export_node_subgraph(Node.id, compilation_unit_node_id) separator '\n\t')
		from Node
		where Node.compilationUnitId = compilation_unit_id
		into node_subgraph;
	if node_subgraph is null then
		set node_subgraph = '';
	end if;
	return export_subgraph(
		export_node(compilation_unit_node_id, compilation_unit_node_id, '"#ffeeee"'),
		'',
		node_subgraph
	);
end $$
delimiter ;

drop function if exists export_node_subgraph;

delimiter $$
create function export_node_subgraph(node_id bigint(20), compilation_unit_node_id text) returns text
begin
	declare node_node_id text;
	declare node_node_label text;
	declare null_check_subgraph text;
	select concat('Node', node_id)
		into node_node_id;
	select concat('<', escape_label(window), '<br />', beginLine, ':', beginColumn, '-', endLine, ':', endColumn, '>')
		from NodeWithWindowView
		where id = node_id
		into node_node_label;
	select group_concat(export_null_check_subgraph(NullCheck.id, node_node_id) separator '\n\t')
		from NullCheck
		where NullCheck.nodeId = node_id
		into null_check_subgraph;
	if null_check_subgraph is null then
		set null_check_subgraph = '';
	end if;
	return export_subgraph(
		export_node(node_node_id, node_node_label, '"#ffdddd"'),
		export_edge(compilation_unit_node_id, node_node_id, '<>'),
		null_check_subgraph
	);
end $$
delimiter ;

drop function if exists export_null_check_subgraph;

delimiter $$
create function export_null_check_subgraph(null_check_id bigint(20), node_node_id text) returns text
begin
	declare null_check_node_id text;
	declare feature_subgraph text;
	select concat('NullCheck', null_check_id)
		into null_check_node_id;
	select group_concat(export_feature_subgraph(Feature.id, null_check_node_id) separator '\n\t')
		from Feature
		where Feature.nullCheckId = null_check_id
		into feature_subgraph;
	return export_subgraph(
		export_node(null_check_node_id, null_check_node_id, '"#eeffee"'),
		export_edge(node_node_id, null_check_node_id, '<>'),
		feature_subgraph
	);
end $$
delimiter ;

drop function if exists export_feature_subgraph;

delimiter $$
create function export_feature_subgraph(feature_id bigint(20), null_check_node_id text) returns text
begin
	declare feature_node_id text;
	declare feature_node_label text;
	declare reason_subgraph text;
	select concat('Feature', feature_id)
		into feature_node_id;
	select concat('<', escape_label(class_name(extractor)), '<br />', class_name(manifestation), '>')
		from Feature
		where id = feature_id
		into feature_node_label;
	select group_concat(export_reason_subgraph(Reason.id, feature_node_id) separator '\n\t')
		from Reason
		where Reason.featureId = feature_id
		into reason_subgraph;
	if reason_subgraph is null then
		set reason_subgraph = '';
	end if;
	return export_subgraph(
		export_node(feature_node_id, feature_node_label, '"#eeeeff"'),
		export_edge(null_check_node_id, feature_node_id, '<>'),
		reason_subgraph
	);
end $$
delimiter ;

drop function if exists export_reason_subgraph;

delimiter $$
create function export_reason_subgraph(reason_id bigint(20), feature_node_id text) returns text
begin
	declare template text default '{node_reason_subgraph}
	{feature_reason_subgraph}';
	declare node_reason_subgraph text;
	declare feature_reason_subgraph text;
	select group_concat(export_node_reason_subgraph(NodeReason.id, feature_node_id) separator '\n\t')
		from NodeReason
		where NodeReason.id = reason_id
		into node_reason_subgraph;
	select group_concat(export_feature_reason_subgraph(FeatureReason.id, feature_node_id) separator '\n\t')
		from FeatureReason
		where FeatureReason.id = reason_id
		into feature_reason_subgraph;
	if node_reason_subgraph is null then
		set node_reason_subgraph = '';
	end if;
	if feature_reason_subgraph is null then
		set feature_reason_subgraph = '';
	end if;
	return replace(
		replace(
			template,
			'{node_reason_subgraph}',
			node_reason_subgraph
		),
		'{feature_reason_subgraph}',
		feature_reason_subgraph
	);
end $$
delimiter ;

drop function if exists export_node_reason_subgraph;

delimiter $$
create function export_node_reason_subgraph(node_reason_id bigint(20), feature_node_id text) returns text
begin
	declare template text default '{node_edge}';
	declare reason_node_node_id text;
	select concat('Node', nodeId)
		from NodeReason
		where id = node_reason_id
		into reason_node_node_id;
	return replace(
		template,
		'{node_edge}',
		export_edge(feature_node_id, reason_node_node_id, '<>')
	);
end $$
delimiter ;

drop function if exists export_feature_reason_subgraph;

delimiter $$
create function export_feature_reason_subgraph(feature_reason_id bigint(20), feature_node_id text) returns text
begin
	declare template text default '{feature_edge}';
	declare reason_feature_node_id text;
	select concat('Feature', reasonFeatureId)
		from FeatureReason
		where id = feature_reason_id
		into reason_feature_node_id;
	return replace(
		template,
		'{feature_edge}',
		export_edge(feature_node_id, reason_feature_node_id, '<>')
	);
end $$
delimiter ;
