package ch.unibe.scg.nullfinder.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EmptyMemberDeclaration;
import com.github.javaparser.ast.body.EmptyTypeDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.MultiTypeParameter;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.IntegerLiteralMinValueExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralMinValueExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.QualifiedNameExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.TypeDeclarationStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public abstract class ConstrainedVisitor<T> extends VoidVisitorAdapter<T> {

	public void startOn(com.github.javaparser.ast.Node node, T argument) {
		while (this.shouldAscendFrom(node, argument)
				&& node.getParentNode() != null) {
			node = node.getParentNode();
		}
		try {
			node.accept(this, argument);
		} catch (StopDescendingException exception) {
			// noop
		}
	}

	@Override
	public void visit(CompilationUnit arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(PackageDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ImportDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(TypeParameter arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(LineComment arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(BlockComment arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(EnumDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(EmptyTypeDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(EnumConstantDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(AnnotationDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(AnnotationMemberDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(FieldDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(VariableDeclarator arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(VariableDeclaratorId arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ConstructorDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(MethodDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(Parameter arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(MultiTypeParameter arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(EmptyMemberDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(InitializerDeclaration arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(JavadocComment arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ClassOrInterfaceType arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(PrimitiveType arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ReferenceType arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(VoidType arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(WildcardType arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ArrayAccessExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ArrayCreationExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ArrayInitializerExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(AssignExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(BinaryExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(CastExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ClassExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ConditionalExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(EnclosedExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(FieldAccessExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(InstanceOfExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(StringLiteralExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(IntegerLiteralExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(LongLiteralExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(IntegerLiteralMinValueExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(LongLiteralMinValueExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(CharLiteralExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(DoubleLiteralExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(BooleanLiteralExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(NullLiteralExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(MethodCallExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(NameExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ObjectCreationExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(QualifiedNameExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ThisExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(SuperExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(UnaryExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(VariableDeclarationExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(MarkerAnnotationExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(SingleMemberAnnotationExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(NormalAnnotationExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(MemberValuePair arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(TypeDeclarationStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(AssertStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(BlockStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(LabeledStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(EmptyStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ExpressionStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(SwitchStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(SwitchEntryStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(BreakStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ReturnStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(IfStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(WhileStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ContinueStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(DoStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ForeachStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ForStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(ThrowStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(SynchronizedStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(TryStmt arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(CatchClause arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(LambdaExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(MethodReferenceExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	@Override
	public void visit(TypeExpr arg0, T arg1) {
		if (!this.shouldDescendInto(arg0, arg1)) {
			throw new StopDescendingException();
		}
		super.visit(arg0, arg1);
	}

	protected boolean shouldAscendFrom(Node node, T argument) {
		return true;
	}

	protected boolean shouldDescendInto(Node node, T argument) {
		return true;
	}
}
