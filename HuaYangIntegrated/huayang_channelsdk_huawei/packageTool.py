# -*- coding: utf-8 -*-
import sys,os
import shutil,zipfile,time,handle_ResSpecicalXml#judgeSo,handle_AndroidManifestorFile,configChannel_AppidKey,getp,decp,AppidKeyConf,find_ChannelRes,apk_util,gowan_SDKMustAppendContent


channelId = '' 
#res_Path= os.path.dirname(os.path.dirname(__file__)) +os.path.sep+"zj_ChannelRes"

def copyFiles(sourceDir,  targetDir): 

    if sourceDir.find(".svn") > 0: 
        return 
    if sourceDir.find(".DS_Store") > 0: 
        return 
    for file in os.listdir(sourceDir): 
        sourceFile = os.path.join(sourceDir,  file) 
        targetFile = os.path.join(targetDir,  file) 
        if os.path.isfile(sourceFile): 
            if not os.path.exists(targetDir):  
                os.makedirs(targetDir)
            #如果是资源文件的话

            if not handle_ResSpecicalXml.handle_ResSpecicalXml(sourceFile,targetFile):
            # elif dirName!='smali':
            #     print sourceFile
            #其他assest和smali文件直接复制
                if not os.path.exists(targetFile) or(os.path.exists(targetFile) and (os.path.getsize(targetFile) != os.path.getsize(sourceFile))):  
                    open(targetFile, "wb").write(open(sourceFile, "rb").read()) 
        if os.path.isdir(sourceFile): 
            First_Directory = False 
            copyFiles(sourceFile, targetFile)

copyFiles(sys.argv[1],sys.argv[2])

# channel_Specical()

# get_ChannelPackage_Start(sys.argv[1])


# parseXml(sys.argv[1],sys.argv[2])

