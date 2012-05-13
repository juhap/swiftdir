<h1>Directory download tool for SWIFT directories</h1>
Java based command line tool for downloading and processing directories 
available through SWIFT Directory Download automation

<h2>Usage</h2>
<code>java -jar swiftdir.jar <command line options></code>

<h3>Command line options</h3>

<table>
<tr><td>-content</td><td>      full or delta</td></tr>
<tr><td>-format</td><td>       file content format (txt, dat, ebcdic, dos)</td></tr>
<tr><td>-platform</td><td>     Windows ZIP or unix compressed file (win, unix)</td></tr>
<tr><td>-product</td><td>      product under product category (bankfile, bicplusiban, ...)</td></tr>
<tr><td>-productline</td><td>  product category (eg. swbankfile, bicdir, bicplusiban, separouting, ...)</td></tr>
<tr><td>-statusFile</td><td>   Path to statusfile (will be empty if download was succesfull, otherwise contains error message on one line)</td></tr>
<tr><td>-targetDir</td><td>    Target directory for the downloaded file</td></tr>
<tr><td>-unzip</td><td>        Uncompress zip file to targetDir (.tar.Z is not supported)</td></tr>
<tr><td>-username</td><td>     username for SWIFT website</td></tr>
<tr><td>-password</td><td>     password for SWIFT website</td></tr>
<tr><td>-year</td><td>         If omitted, defaults to latest available file of selected type</td></tr>
<tr><td>-month</td><td>        If omitted, defaults to month of latest available file of selected type</td></tr>
</table>

The tool can either download the files and save them to disk or it can extract zipped content. Use the unzip -parameter to request unzipping. Unix compressed files (.tar.Z) are not supported.

Statusfile can be used to integrate the tool with external monitoring system. If statusFile parameter is specified, the tool will create the specified file when its executed. If the download was successful, the status file will be empty (length is zero). If there was an error the status file will contain one line that specifies the error in detail.  

For details about the content, format, platform, productline, year and month parameters please refer to the SWIFT documentation, available at: http://www.swift.com/solutions/messaging/information_products/image_doc/BIC_Download_Automation_Interface_Specs.pdf
The application does not validate the values of those parameters. They are passed to SWIFT as given.

<h3>Examples</h3>
Download latest SWIFT Alliance bankfile deltas, extract them to c:/saa/data/UpdateBIC and write download status to c:/dir/status.txt. Example is split to multiple lines for easier read. In real life you should have all options on single line. 

<code>
java -jar swiftdir.jar 
        -username bicdl@example.org  -password <mypassword> 
        -content delta 
        -productline swbankfile -product bankfile 
        -format txt 
        -platform win 
        -targetDir c:/saa/data/UpdateBIC 
        -statusFile c:/dir/status.txt 
        -unzip
</code>

In Windows you can schedule the execution with Windows Task Scheduler.  

<h2>System requirements</h2>
Java 1.6 runtime. Download requires user account on the SWIFT website with appropriate permissions to download the directories. It is highly recommended to create a separate user account that only has access to BIC downloads (since you need to provide the username and password in your scripts, those could become compromised). 

<h2>Build instructions</h2>
The software can be build with Apache Maven. 

Executing mvn with command:

<code>mvn clean install</code>

retrieves dependencies and creates to jar files in the target directory. The larger jar is a standalone version that contains dependent libraries (that one is recommended). 

<h2>License</h2>
This application is licensed with BSD style license. Refer to the accompanied LICENSE file for details.

Software depends on:
- the Apache Commons HTTP Client library, which is licensed under Apache License. 
- Command line processsing is performed with JCommander http://jcommander.org/, which is also licensed
under Apache license. 

Obviously I will make no guarantees that the application will work as advertised. For security reasons it is recommended to compile the application by yourself and to browse through the source code before compiling it.