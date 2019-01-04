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



mvn processor:process
