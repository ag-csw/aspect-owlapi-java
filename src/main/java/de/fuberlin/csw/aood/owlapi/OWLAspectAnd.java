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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to specify a logical conjunction of owl-aspects.
 * 
 * <p>The aspect IRIs are available from the value() element of this annotation. 
 * <p>Note that this annotation is Repeatable in @{@link OWLAspectOr}.
 * 
 * <p>Usage: @OWLAspectAnd({ "iri1", "iri2", ..})
 */
@Documented
@Target({CONSTRUCTOR, METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OWLAspectOr.class)
public @interface OWLAspectAnd {
	/**
	 * logical conjunction of aspects
	 * @return Array of aspect IRIs as Strings. 
	 */
	String[] value(); 
}
