# SFTP Sample

A sample project that talks to an SFTP server. 
It can list directories and download files.

### Demo
Run app and open:
- http://localhost:8080/list?path=/pub/example
- http://localhost:8080/file?path=/pub/example/readme.txt

**Uses the following:**
- spring-integration-sftp - to initialize the SFTP client
- Lombok
- https://test.rebex.net - a read-only FTP server