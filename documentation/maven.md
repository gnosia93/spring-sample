You must specify a valid lifecycle phase or a goal in the format <plugin-prefix>:<goal> 

or <plugin-group-id>:<plugin-artifact-id>[:<plugin-version>]:<goal>. 

Available lifecycle phases are: 

```
validate, 
initialize, 
generate-sources, 
process-sources, 
generate-resources, 
process-resources,
compile, 
process-classes, 
generate-test-sources, 
process-test-sources, 
generate-test-resources, 
process-test-re
sources, 
test-compile, 
process-test-classes, 
test, 
prepare-package, 
package, 
pre-integration-test, 
integration-test, 
post-integration-test, 
verify, 
install, 
deploy, 
pre-clean, 
clean, 
post-clean, 
pre-site, 
site, 
post-site, 
site-deploy
```



```
if you are ever trying to reference output directories in Maven, you should never use a literal value like target/classes. Instead you should use property references to refer to these directories.

    project.build.sourceDirectory
    project.build.scriptSourceDirectory
    project.build.testSourceDirectory
    project.build.outputDirectory
    project.build.testOutputDirectory
    project.build.directory
sourceDirectory, scriptSourceDirectory, and testSourceDirectory provide access to the source directories for the project. outputDirectory and testOutputDirectory provide access to the directories where Maven is going to put bytecode or other build output. directory refers to the directory which contains all of these output directories.
```



```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>io.startup</groupId>
  <artifactId>selmatest</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>selmatest</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding> 
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  
  
  	<!-- scope provided because the processor is only needed at compile time-->
	<dependency>
	    <groupId>fr.xebia.extras</groupId>
	    <artifactId>selma-processor</artifactId>
	    <version>1.0</version>
	    <scope>provided</scope>
	</dependency>
	
	<!-- This is the only real dependency you will have in your binaries -->
	<dependency>
	    <groupId>fr.xebia.extras</groupId>
	    <artifactId>selma</artifactId>
	    <version>1.0</version>
	</dependency>

  	<dependency>
  		<groupId>org.projectlombok</groupId>
  		<artifactId>lombok</artifactId>
  		<version>1.18.4</version>
  	</dependency>
  </dependencies>
  
  
  <build>
  <pluginManagement>
        <plugins>

            <plugin>
                <groupId>org.bsc.maven</groupId>
                <artifactId>maven-processor-plugin</artifactId>
                <version>2.2.4</version>
                <configuration>
                    <defaultOutputDirectory>
                        ${project.build.directory}/generated-sources
                    </defaultOutputDirectory>
                    <processors>
                        <processor>fr.xebia.extras.selma.codegen.MapperProcessor</processor>
                    </processors>
                </configuration>
                <executions>
                    <execution>
                        <id>process</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>process</goal>
                        </goals>
                    </execution>
                </executions>
                <dependencies>
                    <dependency>
                        <groupId>fr.xebia.extras</groupId>
                        <artifactId>selma-processor</artifactId>
                        <version>1.0</version>
                    </dependency>
                </dependencies>
            </plugin>

            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <version>1.9.1</version>
                <executions>
                    <execution>
                        <id>add-source</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>add-source</goal>
                        </goals>
                        <configuration>
                            <sources>
                                <source>${project.build.directory}/generated-sources</source>
                            </sources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-deploy-plugin</artifactId>
                <configuration>
                    <skip>true</skip>
                </configuration>
            </plugin>

        </plugins>
    </pluginManagement>    
    </build>
</project>
```



```
$ mvn processor:process
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------< io.startup:selmatest >------------------------
[INFO] Building selmatest 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- maven-processor-plugin:2.2.4:process (default-cli) @ selmatest ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.236 s
[INFO] Finished at: 2019-01-05T00:18:08+09:00
[INFO] ------------------------------------------------------------------------

```



