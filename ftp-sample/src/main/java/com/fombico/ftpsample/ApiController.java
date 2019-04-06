package com.fombico.ftpsample;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class ApiController {

    private DefaultFtpSessionFactory ftpSessionFactory;

    public ApiController(DefaultFtpSessionFactory ftpSessionFactory) {
        this.ftpSessionFactory = ftpSessionFactory;
    }

    @GetMapping("list")
    public FtpListResponse list(@RequestParam String path) throws IOException {
        FtpSession session = this.ftpSessionFactory.getSession();
        FTPFile[] list = session.list(path);
        List<String> files = Stream.of(list).filter(FTPFile::isFile).map(FTPFile::getName).collect(Collectors.toList());
        List<String> dirs = Stream.of(list).filter(FTPFile::isDirectory).map(FTPFile::getName).collect(Collectors.toList());
        session.close();
        return FtpListResponse.builder()
                .path(path)
                .files(files)
                .directories(dirs)
                .build();
    }

    @GetMapping("file")
    public void file(@RequestParam String path, HttpServletResponse response) throws IOException {
        FtpSession session = this.ftpSessionFactory.getSession();
        if (session.exists(path) && session.list(path)[0].isFile()) {
            session.read(path, response.getOutputStream());
        }
        session.close();
    }
}
