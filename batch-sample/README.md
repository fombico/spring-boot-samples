# Spring Batch Sample

Runs a batch job that takes some numbers and performs an operation on them. Stores the record in a DB.
Can run via a jar:
```
java -jar batch-sample-0.0.1-SNAPSHOT.jar numbers=1,2,3 
```

Or via Intellij where Program Arguments with a `numbers` param in the Run Configuration
(e.g. `numbers=1,-2,5.5`) 

## Required
- Lombok plugin (for `@Sl4fj`), annotation processing on project
- Mysql server (to store batch records on a real db)

## Learnings
- if DB not configured, Spring Batch will use an in-memory option
- Using the same parameters will result in a `JobInstanceAlreadyCompleteException` thrown. 
As a result, the main application adds a random UUID as a parameter to allow for repeats.
- When `spring.batch.job.enabled` is `true`, the `@Job` runs automatically with parameters passed in via `JobParameters`.
These can be accessed via injection:
    ```
    @StepScope
    @Bean
    void foo(@Value("#{jobParameters['numbers']}") String numbers) { ... }
    ```  
    - JobParameters are loaded after beans have been initialized, so they are delayed in a sense.
    As a result, the bean can be initialized with a proxy value that is replaced at runtime.
    See usage of `numberParamListItemReader` and the `PROXY_NUMBERS_PARAM` in `BatchJobConfig`.
    May want to find a cleaner way.
- `ItemReader` need to return `null` at some point or the job will never end
- A `CommandLineJobRunner` can be used, but it is tricky to work with (I do not recommend it)
    ```
    public static void main(String[] args) throws Exception {
        SpringApplication.run(BatchSampleApplication.class, args);
        CommandLineJobRunner.main(new String[]{BatchJobConfig.class.getName(), "job", args[0]});
    }
    ```
    - It loads its own Spring context based on the file passed in. 
    As a result, other auto-detected files (e.g. via `@Component`) aren't found automatically. 
    - Using the main application class is possible, but I haven't got it to load fields via `@Value`
    - Also the job name is hard-coded and needs to match the bean name
    ```
    @Bean
    Job job() {
        return jobs.get("job0")... // not referred to as 'job0' but as 'job'
    }
   ``` 
    - Also you should have `spring.batch.job.enabled` set to `false`