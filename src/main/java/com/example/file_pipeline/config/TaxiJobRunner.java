package com.example.file_pipeline.config;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TaxiJobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job taxiJob;

    public TaxiJobRunner(JobLauncher jobLauncher, Job taxiJob) {
        this.jobLauncher = jobLauncher;
        this.taxiJob = taxiJob;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(" Spring Boot ayağa kalktı. CommandLineRunner üzerinden Batch tetikleniyor");

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(taxiJob, jobParameters);

            System.out.println("Batch işi başarıyla tamamlandı");
        } catch (Exception e) {
            System.err.println("Batch çalışırken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}