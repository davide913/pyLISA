package it.unive.pylisa.libraries.pandas;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.statement.BinaryExpression;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.PluggableStatement;
import it.unive.lisa.program.cfg.statement.Statement;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.heap.HeapAllocation;
import it.unive.lisa.type.Type;
import it.unive.pylisa.symbolic.DataFrameConstant;

public class ReadCsv extends BinaryExpression implements PluggableStatement {
	private Statement st;

	protected ReadCsv(CFG cfg, CodeLocation location, String constructName, Type staticType, Expression left,
			Expression right) {
		super(cfg, location, constructName, staticType, left, right);
	}

	@Override
	final public void setOriginatingStatement(Statement st) {
		this.st = st;
	}

	public static ReadCsv build(CFG cfg, CodeLocation location, Expression[] exprs) {
		return new ReadCsv(cfg, location, "read_csv", PyDataframeType.INSTANCE, exprs[0], exprs[1]);
	}

	@Override
	protected <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
					InterproceduralAnalysis<A, H, V, T> interprocedural,
					AnalysisState<A, H, V, T> state,
					SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> expressions)
					throws SemanticException {
		HeapAllocation alloc = new HeapAllocation(PyDataframeType.INSTANCE, this.getLocation());
		AnalysisState<A, H, V, T> afterAlloc = state.smallStepSemantics(alloc, st);
		DataFrameConstant constant = new DataFrameConstant(PyDataframeType.INSTANCE, right, this.getLocation());
		AnalysisState<A, H, V, T> assigned = state.bottom();
		for (SymbolicExpression exp : afterAlloc.getComputedExpressions())
			assigned = assigned.lub(afterAlloc.assign(exp, constant, st));

		return new AnalysisState<A, H, V, T>(assigned.getState(), afterAlloc.getComputedExpressions());
	}
}