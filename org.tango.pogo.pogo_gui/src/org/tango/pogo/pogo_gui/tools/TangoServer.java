//+======================================================================
//
// Project:   Tango
//
// Description:	java source code DEFINING A tango server (used by multi class server)
//
// $Author: verdier $
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2009,2010,2011,2012,2013,2014
//						European Synchrotron Radiation Facility
//                      BP 220, Grenoble 38043
//                      FRANCE
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

package org.tango.pogo.pogo_gui.tools;

import fr.esrf.tango.pogo.pogoDsl.PogoMultiClasses;

//===============================================================
//===============================================================
public class TangoServer {
    public String name = "";
    public String sourcePath = "";
    public String description = "";
    public String title = "";
    public String license = "GPL";

    //===========================================================
    public TangoServer() {

    }

    //===========================================================
    public TangoServer(PogoMultiClasses multiClasses) {
        this.name = multiClasses.getName();
        this.sourcePath = multiClasses.getSourcePath();
        this.description = multiClasses.getDescription();
        this.title = multiClasses.getTitle();
        this.license = multiClasses.getLicense();
    }

    //===========================================================
    public String toString() {
        return name;
    }
    //===========================================================
}
//===============================================================
//===============================================================
