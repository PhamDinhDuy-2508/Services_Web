package com.Search_Thesis.Search_Thesis.Services.BackupService;

import org.springframework.stereotype.Service;

@Service
public class BackupService {
    String userId ;
    String Id ;
    private  Backup backupProcess  ;

    public BackupService() {
    }

    public BackupService(String userId, String id) {
        this.userId = userId;
        Id = id;
    }

    public void setBackupProcess(Backup backupProcess) {
        this.backupProcess = backupProcess;
    }
    public  void Backup() {
        backupProcess.BackUpToDatabase(this.userId,Id);
    }
}
