package com.fombico.sftpsample;

import com.jcraft.jsch.ChannelSftp;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
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

    private DefaultSftpSessionFactory sftpSessionFactory;

    public ApiController(DefaultSftpSessionFactory sftpSessionFactory) {
        this.sftpSessionFactory = sftpSessionFactory;
    }

    @GetMapping("list")
    public SftpListResponse list(@RequestParam String path) throws IOException {
        SftpSession session = sftpSessionFactory.getSession();
        ChannelSftp.LsEntry[] list = session.list("/" + path);
        List<String> files = Stream.of(list).filter(lsEntry -> lsEntry.getAttrs().isReg())
                .map(ChannelSftp.LsEntry::getFilename)
                .collect(Collectors.toList());
        List<String> dirs = Stream.of(list).filter(lsEntry -> lsEntry.getAttrs().isDir())
                .map(ChannelSftp.LsEntry::getFilename)
                .collect(Collectors.toList());
        session.close();
        return SftpListResponse.builder()
                .path(path)
                .files(files)
                .directories(dirs)
                .build();
    }

    @GetMapping("file")
    public void file(@RequestParam String path, HttpServletResponse response) throws IOException {
        SftpSession session = sftpSessionFactory.getSession();
        if (session.exists(path)) {
            session.read(path, response.getOutputStream());
        }
        session.close();
    }

}
