package ru.bati4eli.smartcloud.android.client.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

import java.io.File;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "local_file")
public class LocalFileDao {

    @DatabaseField(generatedId = true)
    private Long fileId;

    @DatabaseField(canBeNull = false)
    private String fileName;

    @DatabaseField
    private String path;

    @DatabaseField
    private String parentPath = "";

    @DatabaseField
    private Long parentId = -1L;

    @DatabaseField
    private boolean isFavorite = false;

    @DatabaseField
    private int version = 0;

    @DatabaseField
    private LocalDateTime created = LocalDateTime.now();

    @DatabaseField
    private LocalDateTime lastModify = LocalDateTime.now();

    @DatabaseField
    private Long size = 0L;

    @DatabaseField
    private String shortSize = "";

    @DatabaseField
    private int nsfwLevel = 0;

    @DatabaseField
    private int fileType = LocalTypeOfFile.FILE.getValue();

    public LocalTypeOfFile getFileType() {
        return LocalTypeOfFile.fromValue(fileType);
    }

    public void setFileType(LocalTypeOfFile fileType) {
        this.fileType = fileType.getValue();
    }

    public String getFullName(String localMainFolder) {
        if (localMainFolder == null || localMainFolder.isEmpty()) {
            localMainFolder = ParametersUtil.getMainFolder();
        }

        if (getFileType() == LocalTypeOfFile.ROOT) {
            return localMainFolder;
        }

        if (parentPath == null || parentPath.isEmpty()) {
            return combine(localMainFolder, fileName);
        }

        return combine(localMainFolder, parentPath, fileName);
    }

    public File getFileSystemInfo(String localMainFolder) {
        String path = getFullName(localMainFolder);
        File file = new File(path);
        return file;
    }

    public String getExternalPath() {
        String simplifiedPath = MyUtils.simplifyPath(getFullName(ParametersUtil.getMainFolder()), null);
        return "/ROOT/" + simplifiedPath;
    }

    // Methods for path combination
    private static String combine(String... paths) {
        StringBuilder combinedPath = new StringBuilder();
        for (String path : paths) {
            if (combinedPath.length() > 0 && !combinedPath.toString().endsWith("/") && !path.startsWith("/")) {
                combinedPath.append("/");
            }
            combinedPath.append(path.replace("\\", "/"));
        }
        return combinedPath.toString();
    }
}
