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

package fr.esrf.tango.pogo.generator.common


import static extension fr.esrf.tango.pogo.generator.common.StringUtils.*

class Headers {


	//def cvsEscaped (String s)       { "$"   + s + "  $"   }
	//def cvsEscapedForVar (String s) { "\"$" + s + "  $\"" }

	//======================================================
	// generic file header
	//======================================================
	def fileHeader(String fileName, String description, String title, String license, String copyright) {
		"//=============================================================================\n" +
		"//\n" +
		"// file :        "+ fileName + "\n" +
		"//\n" +
		"// description : " + description.comments("//               ") + "\n" +
		"//\n" +
		"// project :     " + title + "\n" +
		"//\n" + licenseText(license, "// ") +
		"//\n" + getCopyrightCommented(copyright) +
		"//\n" + 
		"//=============================================================================\n" +
		"//                This file is generated by POGO\n" +
		"//        (Program Obviously used to Generate tango Object)\n" +
		"//=============================================================================\n"
	}

	//======================================================
	//	Makefile header
	//======================================================
	def makefileHeader(String project, boolean cmake) '''
		#=============================================================================
		#
		??IF cmake??
			# file :        CMakeLists.txt
			#
			# description : File to generate a TANGO device server using cmake.
		??ELSE??
			# file :        Makefile
			#
			# description : Makefile to generate a TANGO device server.
		??ENDIF??
		#
		# project :     ??project??
		#
		#=============================================================================
		#                This file is generated by POGO
		#        (Program Obviously used to Generate tango Object)
		#=============================================================================
		#
		#
	'''	
}