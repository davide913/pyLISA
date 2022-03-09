package it.unive.pylisa.analysis.dataframes.transformation.operations;

import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.pylisa.analysis.dataframes.transformation.Names;

public class ColAccess extends DataframeOperation {

	private final Names cols;

	public ColAccess(CodeLocation where, Names cols) {
		super(where);
		this.cols = cols;
	}

	public Names getCols() {
		return cols;
	}

	@Override
	protected boolean lessOrEqualSameOperation(DataframeOperation other) throws SemanticException {
		ColAccess o = (ColAccess) other;
		return cols.lessOrEqual(o.cols);
	}

	@Override
	protected DataframeOperation lubSameOperation(DataframeOperation other) throws SemanticException {
		ColAccess o = (ColAccess) other;
		return new ColAccess(loc(other), cols.lub(o.cols));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cols == null) ? 0 : cols.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColAccess other = (ColAccess) obj;
		if (cols == null) {
			if (other.cols != null)
				return false;
		} else if (!cols.equals(other.cols))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "access_cols" + cols;
	}
}