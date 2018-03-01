Coding Exercise
===============

Readme
------



Dear IT,

At first, I want to thank you for the assignment. I have learnt tones of exciting new things in programming in different platforms with the feeling of freedom with open source libraries. 
I mostly played it, build and breake. Path relations were so mysterious first, but after grasping, it is much more easier to manage javas and classes than .net assemblies. Doing so, I learnt how to use develop and debug tools of eclipse and oracle jdeveloper and other things.

Problem

Solving the problem in several different approach was my purpose first, but than focused to solving the job. But more than solving the problem, I am impressed by the power of java-linux-oracle kind of things.

This is my first java project, however, object oriented paradigm in coding is always the same. Although, java is much more agile and attractive. I liked it.

Audit-reporter-blank

I preferred a modular open/close approach to solve the problem for the scenario has the potential of over demand for new features from clients overtime. I preferred managing the main logical subtrees from main method using the differently configured constructors by automatic command line recognition process that I created.

By the way, every class has its main method in java, how awesome.

Mostly using boolean types, I finally have the control of the whole ui and back service. I created nested classes for sub-types as poco objects, utilisations. For example, By defining each run mode using a boolean enums like types, the multiple control of the services become easier for user.

Basic Run Modes;

The thread starts with initialisation and configuration methods in which input argument restraint and main run mode definitions are configured. There are 4 different mode which are csv mode, txt mode, filtered mode (top N etc) and regular mode, which are inter operable.

Sometimes I violated solid principles, but my main motivation was to get the job done in an understandable and maintainable way. I might have chosen awkward data types or unnecessary loops to discover tools. I will improve myself overtime.ß

More than anything, I learnt bash, which is beautiful. I knew a little via git but this is different. Along with, I grasp the folder architect of java projects and path relations, which is crucial to understand dependencies for me.

I tried commons cli library to pars the arguments to save time but, I could not build the project in my windows after using it, I do not understand why, new jar references somehow could not resolved by windows maybe, so made another start over. In fact I tried to use open class files of apache by configuring build path from eclipse gui, but I could not reference my classes to them in my workspace. 

Thank you all for introducing me to the Java  world. Hoping to meet u all

Best regards,

Olgun Erguzel





ACME has a utility which generates audit reports - lists of files owned by
users. Substitute JAVA_HOME with your JVM installation and you can build & run
the utility like this:

	export JAVA_HOME=/usr/lib/jvm/java-7-oracle
	$JAVA_HOME/bin/javac -sourcepath src src/com/reengen/utils/auditreporter/Runner.java -d bin
	$JAVA_HOME/bin/java -cp bin com.reengen.utils.auditreporter.Runner resources/users.csv resources/files.csv 


Example output:

	Audit Report
	============
	## User: jpublic
	* audit.xlsx ==> 1638232 bytes
	* movie.avi ==> 734003200 bytes
	* marketing.txt ==> 150680 bytes
	## User: atester
	* pic.jpg ==> 5372274 bytes
	* holiday.docx ==> 570110 bytes


This util works, but marketing department keeps asking for new
functionality.


Your Task
---------

Your task is to add new features - *CSV output* and *TOP #n files report*.


### CSV Output

When run with `-c` flag print the report in csv instead of plain text:

	jpublic,audit.xlsx,1638232
	jpublic,movie.avi,734003200
	jpublic,marketing.txt,150680
	atester,pic.jpg,5372274
	atester,holiday.docx,570110


### TOP #n files

When run with `--top n` should print n-largest files sorted by size, e.g., `--top 3`:

	Top #3 Report
	=============
    * movie.avi ==> user jpublic, 734003200 bytes
    * pic.jpg ==> user atester, 5372274 bytes
    * audit.xlsx ==> user jpublic, 1638232 bytes

Top #n should have a corresponding csv format:

	movie.avi,jpublic,734003200
    pic.jpg,atester,5372274
    audit.xlsx,jpublic,1638232



Other Considerations
--------------------


### Refactoring

Refactor the code as needed, but do not over-do it.


### Tests

Please include a test code.


### External libraries

Include them if you think they will help you get your job done. You can also
include a simple build script to make it more straightforward to deploy. We
should be able to build & run the util on Windows.


### Readme/manual

If you want to differ from the spec, please include a short note what was done
and why. Include a short note how to run your code. If you depend on a
convention (e.g. order of arguments `-c` `--top n`) please document it.

