package com.fombico.batchsample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BatchJobConfigTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    BatchJobConfig batchJobConfig;

    @MockBean
    ResultService resultService;

    @Test
    public void job_completesSuccessfully_sendsResults() throws Exception {
        Map<String, JobParameter> params = new HashMap<>();
        params.put("numbers", new JobParameter("-1,2.5,-3,4,5"));
        JobParameters jobParameters = new JobParameters(params);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        // last step is chunked to size 2
        verify(resultService).sendResult(Arrays.asList(1d, 6.25));
        verify(resultService).sendResult(Arrays.asList(9d, 16d));
        verify(resultService).sendResult(Arrays.asList(25d));
    }

    @Test(expected = JobInstanceAlreadyCompleteException.class)
    public void job_withSameParameters_throwsJobInstanceAlreadyCompleteException() throws Exception {
        Map<String, JobParameter> params = new HashMap<>();
        params.put("numbers", new JobParameter("-1,2"));
        JobParameters jobParameters = new JobParameters(params);

        JobExecution firstExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertThat(firstExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        jobLauncherTestUtils.launchJob(jobParameters);
    }

}