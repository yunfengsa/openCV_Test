#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include <syslog.h>

using namespace std;
using namespace cv;
extern "C" {
jstring
Java_com_exexample_opencvandar_NdkLoader_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "opencv图像处理";
    return env->NewStringUTF(hello.c_str());
}
jstring
Java_com_exexample_opencvandar_NdkLoader_validate(
        JNIEnv *env,
        jobject,
        jlong addrGray,
        jlong addrRgba){
    std::string hello2 = "Hello from validate";
    return env->NewStringUTF(hello2.c_str());
}

CascadeClassifier face_cascade,eyes_cascade;
JNIEXPORT void JNICALL
Java_com_exexample_opencvandar_NdkLoader_detectFace
        (JNIEnv *env, jclass type,jstring eyepath, jstring cascade,jlong frame)
{
    const char *str_cascade,*str_eye;
    str_cascade = env->GetStringUTFChars(cascade,NULL);
    str_eye=env->GetStringUTFChars(eyepath,NULL);
    if(face_cascade.empty()||eyes_cascade.empty()){

        face_cascade.load(str_cascade);
        eyes_cascade.load(str_eye);
        syslog(1000,"load sucess---------------------");
    }
    Mat& srcFrm=*(Mat*)frame;
    vector<Rect> faces;
    Mat fram_gray;
    cvtColor(srcFrm,fram_gray,COLOR_BGR2GRAY);
    equalizeHist(fram_gray,fram_gray);
    face_cascade.detectMultiScale(fram_gray, faces,
                                  1.1, 3,0|CASCADE_SCALE_IMAGE, Size(30, 30));

    for(size_t i = 0; i < faces.size(); i++)
    {
        // 检测到人脸中心
        Point center(faces[i].x + faces[i].width/2,
                     faces[i].y + faces[i].height/2);
        Mat face = fram_gray(faces[i]);
        std::vector<Rect> eyes;
        // 在人脸区域检测人眼
        eyes_cascade.detectMultiScale(face, eyes, 1.1, 2,
                                      0 |CASCADE_SCALE_IMAGE, Size(30, 30) );
        if(eyes.size() > 0)
            // 绘制人脸
            ellipse(srcFrm, center, Size(faces[i].width/2,
                                        faces[i].height/2),
                    0, 0, 360, Scalar( 255, 0, 255 ), 4, 8, 0 );
    }
//    jintArray faceArray;
//    CvHaarClassifierCascade *cv_cascade = (CvHaarClassifierCascade*)cvLoad( str_cascade );
//    IplImage *image = cvLoadImage( str_filename, 1 );
//
//    if(image!=0){
//
//        CvMemStorage* storage = cvCreateMemStorage(0);
//        CvSeq* faces;
//
//        //double t = (double)cvGetTickCount();
//        /* use the fastest variant */
//        faces = cvHaarDetectObjects( image, cv_cascade, storage, 1.2, 2,
//                                     CV_HAAR_DO_CANNY_PRUNING, cvSize(width, height) );
//        //t = (double)cvGetTickCount() - t;
//        //printf( "detection time = %gms\n", t/((double)cvGetTickFrequency()*1000.) );
//
//
//        const int total = faces->total;
//
//        faceArray = env-> NewIntArray(4*total);
//        jint faceBuf[4];
//
//        for( int i = 0; i < total; i++ )
//        {
//            CvRect face_rect = *(CvRect*)cvGetSeqElem( faces, i );
//            int pointX = face_rect.x;
//            int pointY = face_rect.y;
//            int faceWidth = face_rect.width;
//            int faceHeight = face_rect.height;
//
//            //printf("i %d, x %d, y %d, width %d, height %d\n",
//            //        i,pointX,pointY,faceWidth,faceHeight);
//
//
//            faceBuf[0] = pointX;
//            faceBuf[1] = pointY;
//            faceBuf[2] = faceWidth;
//            faceBuf[3] = faceHeight;
//
//
//            env->SetIntArrayRegion(faceArray,i*4,4,faceBuf);
//
//        }
//
//        cvReleaseMemStorage( &storage );
//        cvReleaseImage( &image );
//    }
//    cvReleaseHaarClassifierCascade( &cv_cascade );
//
//
//    env->ReleaseStringUTFChars(cascade, str_cascade);
//    env->ReleaseStringUTFChars(filename, str_filename);
//    return faceArray;
}



}
//图像处理
extern "C"
JNIEXPORT jintArray JNICALL
Java_com_exexample_opencvandar_NdkLoader_getGrayImage(
        JNIEnv *env,
        jclass type,
        jintArray pixels_,
        jint w, jint h) {
    jint *pixels = env->GetIntArrayElements(pixels_, NULL);
    // TODO
    if(pixels==NULL){
        return NULL;
    }
    cv::Mat imgData(h, w, CV_8UC4, pixels);
    uchar *ptr = imgData.ptr(0);
    for (int i = 0; i < w * h; i++) {
        int grayScale = (int) (ptr[4 * i + 2] * 0.299 + ptr[4 * i + 1] * 0.587
                               + ptr[4 * i + 0] * 0.114);
        ptr[4 * i + 1] = (uchar) grayScale;
        ptr[4 * i + 2] = (uchar) grayScale;
        ptr[4 * i + 0] = (uchar) grayScale;
    }

    int size = w * h;
    jintArray result = env->NewIntArray(size);
    env->SetIntArrayRegion(result, 0, size, pixels);
    env->ReleaseIntArrayElements(pixels_, pixels, 0);
    return result;
}


extern "C"
JNIEXPORT jintArray JNICALL Java_com_exexample_opencvandar_NdkLoader_gestureDetection2
        (JNIEnv *env, jobject obj, jstring sImage, jstring sDatadir)
{   static const double SCALE_FACTOR   = 1.1;
    static const int    MIN_NEIGHBORS  = 3;
    static const int    DET_FLAGS = 0;


    static const char *CASCADE_NAME = "/facear.xml";


    static CvHaarClassifierCascade *pgCascade;
    static CvMemStorage *pgStorage;
    const char * str_sImage ,* str_sDatadir;
    jintArray faceArray;
    str_sImage =  env->GetStringUTFChars(sImage, NULL);
    str_sDatadir = env->GetStringUTFChars(sDatadir,NULL);
    jint faceBuf[4];
    if (!pgCascade) // first time? if so, must init detector data structures
    {
        char sCascadePath[256];
        strcpy(sCascadePath,str_sDatadir);
        strcat(sCascadePath,"/");
        strcat(sCascadePath,CASCADE_NAME);
        pgCascade = (CvHaarClassifierCascade*)cvLoad(sCascadePath, 0, 0, 0);
        if(!pgCascade)
            printf("can't load %s", sCascadePath);
        pgStorage = cvCreateMemStorage(0);
        if (!pgStorage)
            printf("out of memory (cvCreateMemStorage failed)");

        env->ReleaseStringUTFChars(sDatadir, str_sDatadir);
    }
    cvClearMemStorage(pgStorage);
    IplImage* pImage = cvLoadImage(str_sImage, CV_LOAD_IMAGE_GRAYSCALE);
    cvResize(pImage, pImage, CV_INTER_LINEAR);
    cvEqualizeHist(pImage, pImage);


    CvSeq* pFaces = cvHaarDetectObjects(pImage, pgCascade, pgStorage,
                                        SCALE_FACTOR, MIN_NEIGHBORS, DET_FLAGS,
                                        cvSize((pImage->width)/4,
                                               (pImage->width)/4));




    if (pFaces->total)  // success?
    {
        // get most central face


        faceArray = env-> NewIntArray(4*pFaces->total);
        int iSelectedFace = 0;
        double MaxOffset = FLT_MAX; // max abs dist from face center to image center
        for (int iFace = 0; iFace < pFaces->total; iFace++)
        {
            CvRect* r = (CvRect*)cvGetSeqElem(pFaces, iFace);
            double Offset = fabs(r->x + r->width/2.0 - pImage->width/2.0);
            if (Offset < MaxOffset)
            {
                MaxOffset = Offset;
                iSelectedFace = iFace;
            }
        }
        CvRect* r = (CvRect*)cvGetSeqElem(pFaces, iSelectedFace);


        // convert x and y (OpenCV coords at corner of face box) to cartesian
        // coords (with x,y at center of face box)


        int x = r->x - pImage->width/2 + r->width/2;
        int y = pImage->height/2 - r->y - r->height/2;


        int width = r->width;
        int height = r->height;


        faceBuf[0]=x;
        faceBuf[1]=y;
        faceBuf[2]=width;
        faceBuf[3]=height;


        env->SetIntArrayRegion(faceArray,0,4,faceBuf);
    }
    cvReleaseImage(&pImage);
    cvReleaseMemStorage( &pgStorage );


    env->ReleaseStringUTFChars(sImage, str_sImage);
    env->ReleaseStringUTFChars(sDatadir, str_sDatadir);


    return faceArray;   // return true on success


}


