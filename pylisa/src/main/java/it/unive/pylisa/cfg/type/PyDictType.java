package it.unive.pylisa.cfg.type;

import java.util.Collection;
import java.util.Set;

import it.unive.lisa.program.CompilationUnit;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.UnitType;
import it.unive.lisa.type.Untyped;
import it.unive.pylisa.libraries.LibrarySpecificationProvider;

public class PyDictType implements UnitType {

	/**
	 * The unique singleton instance of this type.
	 */
	public static final PyDictType INSTANCE = new PyDictType();

	private PyDictType() {
	}

	@Override
	public String toString() {
		return "dict";
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		return other instanceof PyDictType || other instanceof Untyped;
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other == this)
			return this;
		else
			return Untyped.INSTANCE;
	}

	@Override
	public Collection<Type> allInstances() {
		return Set.of(INSTANCE);
	}

	@Override
	public CompilationUnit getUnit() {
		return LibrarySpecificationProvider.getLibraryUnit(LibrarySpecificationProvider.DICT);
	}
}