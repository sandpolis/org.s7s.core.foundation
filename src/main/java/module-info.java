//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
open module com.sandpolis.core.foundation {
	exports com.sandpolis.core.foundation.config;
	exports com.sandpolis.core.foundation.cstruct;
	exports com.sandpolis.core.foundation.util;
	exports com.sandpolis.core.foundation;

	requires com.google.common;
	requires com.google.protobuf;
	requires java.prefs;
	requires java.xml;
	requires org.slf4j;
}
