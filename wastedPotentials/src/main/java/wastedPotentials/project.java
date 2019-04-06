package wastedPotentials;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import wastedPotentials.Suite.Class;
import wastedPotentials.Suite.Parameter;
import wastedPotentials.Suite.Test;



public class project {
	/*
	 * <modelVersion>4.0.0</modelVersion>
	 * <groupId>com.hackathon.beANavigator</groupId>
	 * <artifactId>wastedPotentials</artifactId> <version>0.0.1-SNAPSHOT</version>
	 * <name>autoSeleniumProjectGenerator</name> <description>It creates a basic
	 * skeleton a selenium project along with testNG XML file</description>
	 * <dependencies> <!--
	 * https://mvnrepository.com/artifact/com.codoid.products/fillo --> <dependency>
	 * <groupId>com.codoid.products</groupId> <artifactId>fillo</artifactId>
	 * <version>
	 */
	
	 @JacksonXmlProperty(localName = "modelVersion")
	      private modelVersion mv;
	 @JacksonXmlProperty(localName="groupId")
	 private groupId gid;
	 @JacksonXmlProperty(localName = "name")
	 private name nm;
	 @JacksonXmlProperty(localName = "description")
	 private description descr;
	 @JacksonXmlProperty(localName = "version")
	 private version vers;
	 @JacksonXmlProperty(localName = "dependencies")
	 
	 private dependencies dependencies;
	    @JacksonXmlElementWrapper(useWrapping = false)
	    private List < dependency > dependencieslist;

	    public project() {
	      //  this.name = name;
	        this.dependencieslist = new ArrayList <dependency > ();
	        dependencieslist.add(new dependency("d1"));
	    }

	 	}
class dependencies {

    @JacksonXmlProperty(localName = "dependencies")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List < dependency > dependencies;

    public dependencies() {
        this.dependencies = new ArrayList < dependency > ();
    }

    public void addDependencies(String name) {
        this.dependencies.add(new dependency(name));
    }
}

class dependency {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    dependency(String name) {
        this.name = name;
    }
 
}
class modelVersion{
	@JacksonXmlText
	String a;
	modelVersion(String x){
		this.a=x;
	}
	
}
class groupId{
	@JacksonXmlText
	String a="01";
}
class name{
	@JacksonXmlText
	String a="01";
}
class description{
	@JacksonXmlText
	String a="01";
}
class version{
	@JacksonXmlText
	String a="01";
}
