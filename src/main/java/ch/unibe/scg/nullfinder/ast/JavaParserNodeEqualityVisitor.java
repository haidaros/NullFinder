package ch.unibe.scg.nullfinder.ast;

import com.github.javaparser.ast.ImportDeclaration;
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

public class JavaParserNodeEqualityVisitor extends VoidVisitorAdapter<Node> {

	protected com.github.javaparser.ast.Node javaParserNode;

	public com.github.javaparser.ast.Node getJavaParserNode() {
		if (this.javaParserNode == null) {
			throw new IllegalStateException("Could not find matching node");
		}
		return this.javaParserNode;
	}

	@Override
	public void visit(com.github.javaparser.ast.CompilationUnit javaParserNode,
			Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);
	}

	@Override
	public void visit(PackageDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ImportDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(TypeParameter javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(LineComment javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(BlockComment javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ClassOrInterfaceDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(EnumDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(EmptyTypeDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(EnumConstantDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(AnnotationDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(AnnotationMemberDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(FieldDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(VariableDeclarator javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(VariableDeclaratorId javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ConstructorDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(MethodDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(Parameter javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(MultiTypeParameter javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(EmptyMemberDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(InitializerDeclaration javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(JavadocComment javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ClassOrInterfaceType javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(PrimitiveType javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ReferenceType javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(VoidType javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(WildcardType javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ArrayAccessExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ArrayCreationExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ArrayInitializerExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(AssignExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(BinaryExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(CastExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ClassExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ConditionalExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(EnclosedExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(FieldAccessExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(InstanceOfExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(StringLiteralExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(IntegerLiteralExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(LongLiteralExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(IntegerLiteralMinValueExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(LongLiteralMinValueExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(CharLiteralExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(DoubleLiteralExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(BooleanLiteralExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(NullLiteralExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(MethodCallExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(NameExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ObjectCreationExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(QualifiedNameExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ThisExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(SuperExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(UnaryExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(VariableDeclarationExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(MarkerAnnotationExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(SingleMemberAnnotationExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(NormalAnnotationExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(MemberValuePair javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt javaParserNode,
			Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(TypeDeclarationStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(AssertStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(BlockStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(LabeledStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(EmptyStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ExpressionStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(SwitchStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(SwitchEntryStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(BreakStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ReturnStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(IfStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(WhileStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ContinueStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(DoStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ForeachStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ForStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(ThrowStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(SynchronizedStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(TryStmt javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(CatchClause javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(LambdaExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(MethodReferenceExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	@Override
	public void visit(TypeExpr javaParserNode, Node node) {
		super.visit(javaParserNode, node);
		this.setJavaParserNodeIfEqual(javaParserNode, node);

	}

	protected void setJavaParserNodeIfEqual(
			com.github.javaparser.ast.Node javaParserNode, Node node) {
		if (node.equalsJavaParserNode(javaParserNode)) {
			this.javaParserNode = javaParserNode;
		}
	}

}