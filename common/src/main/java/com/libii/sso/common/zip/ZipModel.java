package com.libii.sso.common.zip;

import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @author lirong
 * @desc
 * @date 2020/03/01 20:58
 */
@Data
public class ZipModel implements Serializable {
    private static final long serialVersionUID = 13846812783412684L;

    /**
     * 解压后文件的名字
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 解压后每个文件的输入流
     */
    private InputStream fileInputstream;
}
