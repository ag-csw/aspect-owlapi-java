/*******************************************************************************
 * This file is part of the Corporate Smart Content Project at Freie Universitaet Berlin, Corporate Semantic Web Group.
 * 
 * This work has been partially supported by the "InnoProfile-Corporate Semantic Web" project funded by the German Federal Ministry of Education and Research (BMBF) and the BMBF Innovation Initiative for the New German Laender - Entrepreneurial Regions.
 * 
 * http://www.corporate-smart-content.de/
 * 
 * Freie Universitaet Berlin
 * Copyright (c) 2013-2016
 * 
 * Institut fuer Informatik
 * Working Group Corporate Semantic Web
 * Koenigin-Luise-Strasse 24-26
 * 14195 Berlin
 * 
 * http://www.mi.fu-berlin.de/en/inf/groups/ag-csw/
 ******************************************************************************/
package de.fuberlin.csw.aood.owlapi;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to specify a logical disjunction of owl-aspects.
 * 
 * <p>The aspects are represented indirectly, as values of annotations of type {@link OWLAspectAnd}.
 * 
 * <p>Usage: {@code @OWLAspectOr}({ @{@code OWLAspectAnd}(...), @{@code OWLAspectAnd}(...), ...}).
 * The outer {@code @OWLAspectOr} can be left out.
 */
@Documented
@Target({CONSTRUCTOR, METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OWLAspectOr {
	/** 
	 * logical disjunction of aspects
	 * @return array of annotations of type OWLAspectAnd for this object (which are combined with Or-Operator) */
	OWLAspectAnd[] value();
}
