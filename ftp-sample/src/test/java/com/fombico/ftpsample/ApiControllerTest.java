package com.fombico.ftpsample;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ApiController.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = FtpConfig.class)
})
public class ApiControllerTest {

    @Autowired
    MockMvc mvc;

    @Value("${ftp.port}") int port;
    @Value("${ftp.username}") String username;
    @Value("${ftp.password}") String password;

    private FakeFtpServer fakeFtpServer;

    @Before
    public void setup() {
        fakeFtpServer = new FakeFtpServer();

        fakeFtpServer.addUserAccount(new UserAccount(username, password, "/fred"));

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/fred/"));
        fileSystem.add(new DirectoryEntry("/fred/docs"));
        fileSystem.add(new DirectoryEntry("/fred/docs/2019"));
        fileSystem.add(new FileEntry("/fred/docs/text.txt", "some text"));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.setServerControlPort(port);

        fakeFtpServer.start();
    }

    @Test
    public void list_listsFiles() throws Exception {
        mvc.perform(get("/list").param("path", "docs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path", is("docs")))
                .andExpect(jsonPath("$.files[0]", is("text.txt")))
                .andExpect(jsonPath("$.directories[0]", is("2019")));
    }

    @Test
    public void file_downloadsTheFile() throws Exception {
        mvc.perform(get("/file").param("path", "docs/text.txt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("some text"));
    }

    @After
    public void teardown() {
        fakeFtpServer.stop();
    }
}