drop view if exists NullCheckConditional;

create view NullCheckConditional as (
	select CompilationUnit.id,
		CompilationUnit.conditionals,
		count(NullCheck.nodeId) as nullChecks
	from CompilationUnit
		left outer join Node on (Node.compilationUnitId = CompilationUnit.id)
		left outer join NullCheck on (NullCheck.nodeId = Node.id)
	group by CompilationUnit.id
);
