drop function if exists page_normalize;

create function page_normalize(page longtext) returns longtext
	return replace(replace(page, '\r\n', '\n'), '\r', '\n');

drop function if exists page_line;

create function page_line(page_normalized longtext, line int(11)) returns longtext
	return substring_index(substring_index(page_normalized, '\n', line), '\n', -1);

drop function if exists page_window;

create function page_window(page_normalized longtext, begin_line int(11), begin_column int(11), end_line int(11), end_column int(11)) returns longtext
	return case (end_line - begin_line)
		when 0 then
			substring(page_line(page_normalized, begin_line), begin_column, end_column - begin_column + 1)
		when 1 then
			concat(
				substring(page_line(page_normalized, begin_line), begin_column),
				'\n',
				substring(page_line(page_normalized, end_line), 1, end_column + 1)
			)
		else
			concat(
				substring(page_line(page_normalized, begin_line), begin_column),
				'\n',
				substring_index(substring_index(page_normalized, '\n', begin_line + 1), '\n', -(end_line - begin_line -1)),
				'\n',
				substring(page_line(page_normalized, end_line), 1, end_column + 1)
			)
	end;


drop view if exists NodeWithWindowView;

create view NodeWithWindowView as (
	select
		Node.id as id,
		Node.compilationUnitId as compilationUnitId,
		Node.className as className,
		Node.beginLine as beginLine,
		Node.beginColumn as beginColumn,
		Node.endLine as endLine,
		Node.endColumn as endColumn,
		page_window(page_normalize(CompilationUnit.source), Node.beginLine, Node.beginColumn, Node.endLine, Node.endColumn) as window
	from Node
		join CompilationUnit on (CompilationUnit.id = Node.compilationUnitId)
);
