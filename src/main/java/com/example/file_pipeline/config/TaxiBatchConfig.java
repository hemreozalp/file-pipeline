package com.example.file_pipeline.config;

import com.example.file_pipeline.TaxiTrip;
import com.example.file_pipeline.TaxiTripInput;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TaxiBatchConfig {

    @Bean
    public FlatFileItemReader<TaxiTripInput> reader() {
        return new FlatFileItemReaderBuilder<TaxiTripInput>()
                .name("taxiTripReader")
                .resource(new FileSystemResource("C:\\Users\\hemre\\Desktop\\projects\\file-pipeline\\src\\main\\resources\\2023_Yellow_Taxi_Trip_Data.csv"))
                .delimited()
                .names("vendorId", "tpepPickupDatetime", "tpepDropoffDatetime", "passengerCount",
                        "tripDistance", "ratecodeID", "storeAndFwdFlag", "puLocationID", "doLocationID",
                        "paymentType", "fareAmount", "extra", "mtaTax", "tipAmount", "tollsAmount",
                        "improvementSurcharge", "totalAmount", "congestionSurcharge", "airportFee")
                .fieldSetMapper(fieldSet -> {
                    TaxiTripInput input = new TaxiTripInput();
                    input.setVendorId(fieldSet.readString("vendorId"));
                    input.setTpepPickupDatetime(fieldSet.readString("tpepPickupDatetime"));
                    input.setTpepDropoffDatetime(fieldSet.readString("tpepDropoffDatetime"));
                    input.setPassengerCount(fieldSet.readString("passengerCount"));
                    input.setTripDistance(fieldSet.readString("tripDistance"));
                    input.setFareAmount(fieldSet.readString("fareAmount"));
                    input.setTipAmount(fieldSet.readString("tipAmount"));
                    input.setTotalAmount(fieldSet.readString("totalAmount"));
                    return input;
                })
                .linesToSkip(1)
                .build();
    }

    @Bean
    public JpaItemWriter<TaxiTrip> writer(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<TaxiTrip>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public Step taxiStep(JobRepository jobRepository,
                         PlatformTransactionManager transactionManager,
                         FlatFileItemReader<TaxiTripInput> reader,
                         JpaItemWriter<TaxiTrip> writer,
                         TaxiTripProcessor processor) {
        return new StepBuilder("taxiStep", jobRepository)
                .<TaxiTripInput, TaxiTrip>chunk(1000, transactionManager) // Her 1000 satırda bir DB'ye toplu yazma yapacak
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job taxiJob(JobRepository jobRepository, Step taxiStep) {
        return new JobBuilder("importTaxiJob", jobRepository)
                .start(taxiStep)
                .build();
    }
}
