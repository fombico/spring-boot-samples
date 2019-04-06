# FTP Sample

A sample project that talks to an FTP server. 
It can list directories and download files.

### Demo
Run app and open:
- http://localhost:8080/list?path=/pub/example
- http://localhost:8080/file?path=/pub/example/readme.txt

**Uses the following:**
- spring-integration-ftp - to initialize the FTP client
- Lombok
- [MockFtpServer](http://mockftpserver.sourceforge.net/index.html) - for tests
- https://test.rebex.net - a read-only FTP server