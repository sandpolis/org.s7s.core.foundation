//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//

plugins {
	id("java-library")
	id("sandpolis-java")
	id("sandpolis-module")
	id("sandpolis-protobuf")
	id("sandpolis-publish")
}

dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
	testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.2")
	testImplementation("net.jodah:concurrentunit:0.4.6")

	// https://github.com/google/guava
	api ("com.google.guava:guava:30.1.1-jre") {
		exclude(group = "com.google.code.findbugs", module = "jsr305")
		exclude(group = "com.google.guava", module = "listenablefuture")
		exclude(group = "org.checkerframework", module = "checker-qual")
		exclude(group = "com.google.errorprone", module = "error_prone_annotations")
		exclude(group = "com.google.j2objc", module = "j2objc-annotations")
	}

	// https://github.com/protocolbuffers/protobuf
	api("com.google.protobuf:protobuf-java:3.18.0")

	// https://github.com/qos-ch/slf4j
	api("org.slf4j:slf4j-api:1.7.30")
}
