package wastedPotentials;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.Spring;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.oracle.webservices.internal.api.message.BasePropertySet;


public class XLSReader {

    private  Fillo fillo;
    private  String filePath;
    String testNg_listenerClasses_csv;
	String testNG_suite_level_paramName_paramValue_csv;
    private Connection connection;
    private ArrayList<ArrayList<String>> arrTestNGSuiteLevelParameters=new ArrayList<ArrayList<String>>();
    private ArrayList<String> arrTestNGListeners= new ArrayList<>();
    private String projectName;
    private String projectDescription;
    private String projectSkeleton;
    private String projectLocation;
    private String testNg_Needed;
    private String maven_Needed;
    private String maven_project_ModelVersion,maven_project_GroupId,maven_project_ArtifactId,maven_project_Version;
    private ArrayList<ArrayList<String>> arrDependenciesMaven=new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>>  arrProjectLevelDetails=new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> arrProjectSkeletons=new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> arrQueries=new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> arrtest_paramName_paramValue_csv=new ArrayList<ArrayList<String>>();
    private ArrayList<String> arrQuery=new ArrayList<>();
    private String pldquery;
    private String tcquery;      
    private String dquery;
    private String psquery;
    public XLSReader(String filePath) throws Exception{
       try {

    	this.fillo = new Fillo();
        this.filePath = filePath;
        connection = fillo.getConnection(this.filePath);
        Recordset queriesRS = connection.executeQuery("select * from queries");
        //queryName	query
         arrQueries= new ArrayList<ArrayList<String>>();
         while(queriesRS.next()) {
         arrQuery.add(queriesRS.getField("queryName"));
         arrQuery.add(queriesRS.getField("query"));
         arrQueries.add(arrQuery);
         arrQuery= new ArrayList<String>();
         }

       // projectName	projectDescription	projectSkeleton	projectLocation
        //testNg_Needed	maven_Needed	maven_project_ModelVersion	
        //maven_project_GroupId	maven_project_ArtifactId	maven_project_Version
        //testNg_listenerClasses_csv	testNG_suite_level_paramName:paramValue_csv
       
         //psdquery  tcquery   dquery    psquery
//pldquery
     

         for(int i=0;i<arrQueries.size();i++) {
        	if(arrQueries.get(i).get(0).equalsIgnoreCase("pldquery")) {
        		pldquery=arrQueries.get(i).get(1);
            	
            }
        	else if(arrQueries.get(i).get(0).equalsIgnoreCase("tcquery")) {
        		tcquery=arrQueries.get(i).get(1);
            	
            }
        	else if(arrQueries.get(i).get(0).equalsIgnoreCase("dquery")) {
        	dquery=arrQueries.get(i).get(1);
}
        	else if(arrQueries.get(i).get(0).equalsIgnoreCase("psquery")) {
	psquery=arrQueries.get(i).get(1);
}
        	else {
        		throw new Exception("query name is invalid");
        	}

        	
        }

         Recordset pldRS=connection.executeQuery(pldquery);
        
		while(pldRS.next()) {
         this.projectName=pldRS.getField("projectName");
         this.projectDescription=pldRS.getField("projectDescription");
         this.projectSkeleton=pldRS.getField("projectSkeleton");
         this.projectLocation=pldRS.getField("projectLocation");
         maven_project_ModelVersion=pldRS.getField("maven_project_ModelVersion");
     	maven_project_GroupId=pldRS.getField("maven_project_GroupId");
     	maven_project_ArtifactId=pldRS.getField("maven_project_ArtifactId");
     	maven_project_Version=pldRS.getField("maven_project_Version");
     	 testNg_listenerClasses_csv=pldRS.getField("testNg_listenerClasses_csv");
    	 testNG_suite_level_paramName_paramValue_csv=pldRS.getField("testNG_suite_level_paramName_paramValue_csv");
         testNg_Needed=pldRS.getField("testNg_Needed");
         maven_Needed=pldRS.getField("maven_Needed");
break;
         }
		pldRS.close();
         Recordset psRS=connection.executeQuery(psquery);
         System.out.println(projectSkeleton);
         while(psRS.next()) {

         	if(projectSkeleton.equalsIgnoreCase(psRS.getField("project_skeleton"))) {
         		createPaths(projectLocation, psRS.getField("projectStructure"));
         	}
         	break;
         }


        if(!maven_Needed.isEmpty() && maven_Needed.equals("Y")) {

        	Recordset dRS=connection.executeQuery(dquery);
        	while(dRS.next()) {
        	ArrayList<String> singledependency= new ArrayList<String>();
        	singledependency.add(dRS.getField("dependencyArtifactId"));
        	singledependency.add(dRS.getField("dependencyGroupId"));
        	singledependency.add(dRS.getField("dependencyVersion"));
            arrDependenciesMaven.add(singledependency);
        	singledependency= new ArrayList<String>();
        	}
        }

        if(!testNg_Needed.isEmpty() && testNg_Needed.equals("Y")) {

        	 	String[] listenerstring= testNg_listenerClasses_csv.split(",");
        	String[] suitelevelparams=testNG_suite_level_paramName_paramValue_csv.split(",");
        	
        	for(String s:listenerstring) {
        		arrTestNGListeners.add(s);
        	}

    		ArrayList<String> x= new ArrayList<String>();

        	for(String s1: suitelevelparams) {
        		String[] s2=s1.split(":");
        		for(String s3:s2) {
        			x.add(s3);
        		}
        		arrTestNGSuiteLevelParameters.add(x);
        		x=new ArrayList<String>();
        		
        	}

        	Recordset tcRS= connection.executeQuery(tcquery);
        	createSuite(this.projectName,tcRS, this.projectLocation );
        	Recordset mdRS= connection.executeQuery(dquery);
        	createMavenPOM(this.maven_project_ModelVersion, this.maven_project_GroupId, this.maven_project_ArtifactId, this.maven_project_Version, this.projectName, this.projectDescription, mdRS,this.projectLocation );
        }
        
      //  testCaseName	testClasses_csv	numberOfInstances	test_paramName_paramValue_csv

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        connection.close();
    }
    }


    public void createSuite(String suitename,Recordset recordset,String basepath) throws FilloException {
    	
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        Suite suite = new Suite(suitename);
        try {
            while (recordset.next()) {
            	String classPath=recordset.getField("classPath");
                String testName = recordset.getField("testCaseName");
                String className = recordset.getField("testClasses_csv");
                String param = "Data";
               // String paramValue = recordset.getField("Data");
                String paramValue="value";
                String numberOfInstances=recordset.getField("numberOfInstances");
                for(int i=1;i<=Integer.parseInt(numberOfInstances);i++) {
                suite.addTest("["+i+"]"+testName, param, paramValue, className,basepath+classPath);
                }
            }
            xmlMapper.writeValue(new File(basepath+"//testng-suite.xml"), suite);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            recordset.close();
        }
    }
public void createMavenPOM(String modelVersion,String groupId,String artifactId,String version,String name,String description,Recordset recordset,String basepath) throws FilloException {
    	
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        //Suite suite = new Suite(suitename);
        project p1= new project();
        try {
		/*
		 * try { while (recordset.next()) { String
		 * classPath=recordset.getField("classPath"); String testName =
		 * recordset.getField("testCaseName"); String className =
		 * recordset.getField("testClasses_csv"); String param = "Data"; // String
		 * paramValue = recordset.getField("Data"); String paramValue="value"; String
		 * numberOfInstances=recordset.getField("numberOfInstances"); for(int
		 * i=1;i<=Integer.parseInt(numberOfInstances);i++) {
		 * suite.addTest("["+i+"]"+testName, param, paramValue,
		 * className,basepath+classPath); } }
		 */
            xmlMapper.writeValue(new File(basepath+"//pom.xml"), p1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            recordset.close();
        }
    }
public void createPaths(String basepath,String codePath) {
System.out.println(basepath);
	StringBuilder s = new StringBuilder(codePath.length());
	s.append(basepath);
	for(int i=1;i<codePath.length()-1;i++) {
	if(codePath.charAt(i)=='[') {
		s.append("\\");
	}
	else if(codePath.charAt(i)==']') {
		s.append("\\..\\");
		
	}
	else if(codePath.charAt(i)==',') {
	s.append("\\..\\");
}
	else {
		s.append(codePath.charAt(i));
	}
	}
	try {
		System.out.println(s);

	    Files.createDirectories(Paths.get(s.toString()));
	} catch (IOException e) {
	    System.err.println("Cannot create directories - " + e);
	}
	 
}
}


