//+======================================================================
//
// Project:   Tango
//
// Description:  source code for Tango code generator.
//
// $Author: verdier $
//
// Copyright (C) :  2004,2005,2006,2007,2008,2009,2009,2010,2011,2012,2013,2014
//					European Synchrotron Radiation Facility
//                  BP 220, Grenoble 38043
//                  FRANCE
//
// This file is part of Tango.
//
// Tango is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// Tango is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Tango.  If not, see <http://www.gnu.org/licenses/>.
//
// $Revision: $
// $Date:  $
//
// $HeadURL: $
//
//-======================================================================

package fr.esrf.tango.pogo.generator.html

import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.IFileSystemAccess
import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource


class HtmlGenerator implements IGenerator {

	@Inject	HtmlIndex		htmlIndex
	@Inject	HtmlDescription	htmlDescription
	@Inject	HtmlCommands	htmlCommands
	@Inject	HtmlAttributes	htmlAttributes

	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		
			htmlIndex.doGenerate(resource, fsa)
			htmlDescription.doGenerate(resource, fsa)	//	Do description, properties and states
			htmlCommands.doGenerate(resource, fsa)
			htmlAttributes.doGenerate(resource, fsa)
	}
}