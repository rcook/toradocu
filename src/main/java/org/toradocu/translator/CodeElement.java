package org.toradocu.translator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.toradocu.util.Distance;

public abstract class CodeElement<T> {
	
	private List<String> identifiers; // Strings that can be used to refer to this code element
	private String stringRepresentation; // String used to build Java conditions
	private T javaCodeElement; // The Java code element this object wraps
	
	public CodeElement(T javaCodeElement, String... identifiers) {
		this.javaCodeElement = javaCodeElement;
		this.identifiers = new ArrayList<>(Arrays.asList(identifiers));
	}

	public void addIdentifier(String indentifier) {
		identifiers.add(indentifier);
	}
	
	public List<String> getIdentifiers() {
		return identifiers;
	}
	
	public String getStringRepresentation() {
		if (stringRepresentation == null) {
			stringRepresentation = buildStringRepresentation();
		}
		return stringRepresentation;
	}
	
	public int getLevenshteinDistanceFrom(String s) {
		return identifiers.stream().map(identifier -> Distance.levenshteinDistance(identifier, s))
								   .min(Comparator.naturalOrder()).orElse(Integer.MAX_VALUE);
	}
	
	public T getJavaCodeElement() {
		return javaCodeElement;
	}

	public abstract String buildStringRepresentation();
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CodeElement)) return false;
		
		CodeElement<?> that = (CodeElement<?>) obj;
		if (this.getIdentifiers().equals(that.getIdentifiers()) && 
			this.getJavaCodeElement().equals(that.getJavaCodeElement()) &&
			this.getStringRepresentation().equals(that.getStringRepresentation())) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getIdentifiers(), getStringRepresentation());
	}
	
	@Override
	public String toString() {
		return getStringRepresentation() + ": " + getIdentifiers();
	}
}