package com.scheduler.config;

import com.scheduler.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<Employee> itemReader,
                   ItemProcessor<Employee, Employee> itemProcessor,
                   ItemWriter<Employee> itemWriter) {

        Step step = stepBuilderFactory.get("ETL-file-load")
                .<Employee, Employee>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        return jobBuilderFactory.get("ETL-LOAD")
                .incrementer((new RunIdIncrementer()))
                .start(step)
                .build();
    }

    @Bean
    public FlatFileItemReader<Employee> itemReader() {
        FlatFileItemReader<Employee> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource(("src/main/resources/employees.csv")));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(linemapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<Employee> linemapper() {
        DefaultLineMapper<Employee> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"id","name","department","salary"});

        BeanWrapperFieldSetMapper<Employee> fieldSetWrapper = new BeanWrapperFieldSetMapper<>();
        fieldSetWrapper.setTargetType(Employee.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetWrapper);

        return defaultLineMapper;
    }
}
