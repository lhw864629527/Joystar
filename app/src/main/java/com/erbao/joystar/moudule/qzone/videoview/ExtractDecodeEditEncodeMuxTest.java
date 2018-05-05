///*
// * Copyright (C) 2013 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.erbao.joystar.moudule.qzone.videoview;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.media.MediaCodec;
//import android.media.MediaCodecInfo;
//import android.media.MediaCodecList;
//import android.media.MediaExtractor;
//import android.media.MediaFormat;
//import android.media.MediaMuxer;
//import android.os.Environment;
//import android.os.HandlerThread;
//import android.test.AndroidTestCase;
//import android.util.Log;
//import android.view.Surface;
//
//
//import com.erbao.joystar.utils.LogUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.util.concurrent.atomic.AtomicReference;
//
//import static android.R.attr.format;
//
///**
// * 对MediaMuxer和MediaCodec编码器的集成测试。
// * <p>
// * <p>它使用MediaExtractor从测试流中获取帧，并将其解码到一个表面，使用一个着色器来编辑它们，并将它们从结果的表面编码，然后使用MediaMuxer将它们写入文件。
// * <p>
// * <p>它现在不检查结果文件是否正确，但是确保在这个过程中没有失败。
// * <p>
// * <p>它还测试了codec配置缓冲区需要从MediaCodec传递到MediaMuxer的方式。
// */
//@TargetApi(18)
//public class ExtractDecodeEditEncodeMuxTest extends AndroidTestCase {
//
//    Context mAppContext;
//
//    public ExtractDecodeEditEncodeMuxTest(Context mAppContext) {
//        this.mAppContext = mAppContext;
//    }
//
//    private static final String TAG = ExtractDecodeEditEncodeMuxTest.class.getSimpleName();
//    private static final boolean VERBOSE = false; // lots of logging
//    /**
//     * How long to wait for the next buffer to become available.
//     */
//    private static final int TIMEOUT_USEC = 10000;
//    /**
//     * Where to output the test files.
//     */
//    private static final File OUTPUT_FILENAME_DIR = Environment.getExternalStorageDirectory();
//    // parameters for the video encoder
//    private static final String OUTPUT_VIDEO_MIME_TYPE = "video/avc"; // H.264 Advanced Video Coding
//    private static final int OUTPUT_VIDEO_BIT_RATE = 2000000; // 2Mbps
//    private static final int OUTPUT_VIDEO_FRAME_RATE = 15; // 15fps
//    private static final int OUTPUT_VIDEO_IFRAME_INTERVAL = 10; // 10 seconds between I-frames
//    private static final int OUTPUT_VIDEO_COLOR_FORMAT =
//            MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface;
//    // parameters for the audio encoder
//    private static final String OUTPUT_AUDIO_MIME_TYPE = "audio/mp4a-latm"; // Advanced Audio Coding
//    private static final int OUTPUT_AUDIO_CHANNEL_COUNT = 2; // Must match the input stream.
//    private static final int OUTPUT_AUDIO_BIT_RATE = 128 * 1024;
//    private static final int OUTPUT_AUDIO_AAC_PROFILE =
//            MediaCodecInfo.CodecProfileLevel.AACObjectHE;
//    private static final int OUTPUT_AUDIO_SAMPLE_RATE_HZ = 44100; // Must match the input stream.
//    /**
//     * Used for editing the frames.
//     * <p>
//     * <p>Swaps green and blue channels by storing an RBGA color in an RGBA buffer.
//     */
//    private static final String FRAGMENT_SHADER =
//            "#extension GL_OES_EGL_image_external : require\n" +
//                    "precision mediump float;\n" +
//                    "varying vec2 vTextureCoord;\n" +
//                    "uniform samplerExternalOES sTexture;\n" +
//                    "void main() {\n" +
//                    "  gl_FragColor = texture2D(sTexture, vTextureCoord).rbga;\n" +
//                    "}\n";
//    /**
//     * Whether to copy the video from the test video.
//     */
//    private boolean mCopyVideo;
//    /**
//     * Whether to copy the audio from the test video.
//     */
//    private boolean mCopyAudio;
//    /**
//     * Width of the output frames.
//     */
//    private int mWidth = -1;
//    /**
//     * Height of the output frames.
//     */
//    private int mHeight = -1;
//    /**
//     * The raw resource used as the input file.
//     */
//    private int mSourceResId;
//    /**
//     * The destination file for the encoded output.
//     */
//    public static  String mOutputFile;
//
//    private GPUImageFilterTools.FilterType mFilterType = GPUImageFilterTools.FilterType.NOFILTER;
//
//    public void setFilterType(GPUImageFilterTools.FilterType filterType) {
//        mFilterType = filterType;
//    }
//
//    /**
//     * @param savePath 保存路径
//     * @throws Throwable
//     */
//    public void testExtractDecodeEditEncodeMuxAudioVideo(String savePath) throws Throwable {
////        setSize(1280, 720);
//        setSize(480, 640);//lhd
//        setOutputFile(savePath);//lhd
////        setSource(R.raw.video_480x360_mp4_h264_500kbps_30fps_aac_stereo_128kbps_44100hz);
//        setCopyAudio();
//        setCopyVideo();
//
//
//        new HandlerThread("completethread") {
//            @Override
//            public void run() {
////                super.run();
//                try {
//                    extractDecodeEditEncodeMux();
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
////        TestWrapper.runTest(this);
//    }
//
//    /**
//     * Sets the test to copy the video stream.
//     */
//    private void setCopyVideo() {
//        mCopyVideo = true;
//    }
//
//    /**
//     * Sets the test to copy the video stream.
//     */
//    private void setCopyAudio() {
//        mCopyAudio = true;
//    }
//
//    /**
//     * Sets the desired frame size.
//     */
//    private void setSize(int width, int height) {
//        if ((width % 16) != 0 || (height % 16) != 0) {
//            Log.w(TAG, "WARNING: width or height not multiple of 16");
//        }
//        mWidth = width;
//        mHeight = height;
//    }
//
//    /**
//     * Sets the raw resource used as the source video.
//     */
////    private void setSource(int resId) {
////        mSourceResId = resId;
////    }
//    private String mPath;
//    private String mPathAudio;
//
//    public void setSource(String path) {
//        mPath = path;
//    }
//    public void setSourceAudio(String path) {
//        mPathAudio = path;
//    }
//
//    /**
//     * Sets the name of the output file based on the other parameters.
//     * <p>
//     * <p>Must be called after {@link #setSize(int, int)} and {@link #(int)}.
//     */
//    private void setOutputFile(String savePath) {
//        StringBuilder sb = new StringBuilder();
////        sb.append(OUTPUT_FILENAME_DIR.getAbsolutePath());//保存的路径
//        sb.append(savePath);
//        sb.append("/cts-media-");
//        sb.append(getClass().getSimpleName());
//        assertTrue("should have called setSource() first", mSourceResId != -1);
//        sb.append('-');
//        sb.append(mSourceResId);
//        if (mCopyVideo) {
//            assertTrue("should have called setSize() first", mWidth != -1);
//            assertTrue("should have called setSize() first", mHeight != -1);
//            sb.append('-');
//            sb.append("video");
//            sb.append('-');
//            sb.append(mWidth);
//            sb.append('x');
//            sb.append(mHeight);
//        }
//        if (mCopyAudio) {
//            sb.append('-');
//            sb.append("audio");
//        }
//        sb.append(".mp4");
//        mOutputFile = sb.toString();
//        Log.i("LHD", "outPath: " + sb.toString());
//    }
//
//    /**
//     * Tests encoding and subsequently decoding video from frames generated into a buffer.
//     * <p>
//     * We encode several frames of a video test pattern using MediaCodec, then decode the output
//     * with MediaCodec and do some simple checks.
//     */
//    private void extractDecodeEditEncodeMux() throws Exception {
//        // Exception that may be thrown during release.
//        Exception exception = null;
//        MediaCodecInfo videoCodecInfo = selectCodec(OUTPUT_VIDEO_MIME_TYPE);
//        if (videoCodecInfo == null) {
//            // Don't fail CTS if they don't have an AVC codec (not here, anyway).
//            Log.e(TAG, "Unable to find an appropriate codec for " + OUTPUT_VIDEO_MIME_TYPE);
//            return;
//        }
//        if (VERBOSE) Log.d(TAG, "video found codec: " + videoCodecInfo.getName());
//        MediaCodecInfo audioCodecInfo = selectCodec(OUTPUT_AUDIO_MIME_TYPE);
//        if (audioCodecInfo == null) {
//            // Don't fail CTS if they don't have an AAC codec (not here, anyway).
//            Log.e(TAG, "Unable to find an appropriate codec for " + OUTPUT_AUDIO_MIME_TYPE);
//            return;
//        }
//        if (VERBOSE) Log.d(TAG, "audio found codec: " + audioCodecInfo.getName());
//        MediaExtractor videoExtractor = null;//音视频的分离器
//        MediaExtractor audioExtractor = null;//音视频的分离器
//        OutputSurfaceWithFilter outputSurface = null;
//        MediaCodec videoDecoder = null;
//        MediaCodec audioDecoder = null;
//        MediaCodec videoEncoder = null;
//        MediaCodec audioEncoder = null;
//        MediaMuxer muxer = null;//音视频的混合器
//        InputSurface inputSurface = null;
//        try {
//            if (mCopyVideo) {
//                System.out.println("2223=======mCopyVideo======");
//                videoExtractor = createExtractor();
//                int videoInputTrack = getAndSelectVideoTrackIndex(videoExtractor);
//                LogUtils.e("====22222===1==="+videoInputTrack);
//                assertTrue("missing video track in test video", videoInputTrack != -1);
//                MediaFormat inputFormat = videoExtractor.getTrackFormat(videoInputTrack);
//                // 我们使用的值是16的倍数，从而避免了在宽度和高度上的设备特定的限制，这些值都是经过测试的设备似乎能够处理的。
//                MediaFormat outputVideoFormat =
//                        MediaFormat.createVideoFormat(OUTPUT_VIDEO_MIME_TYPE, mWidth, mHeight);
//                // Set some properties. Failing to specify some of these can cause the MediaCodec
//                // configure() call to throw an unhelpful exception.
//                outputVideoFormat.setInteger(
//                        MediaFormat.KEY_COLOR_FORMAT, OUTPUT_VIDEO_COLOR_FORMAT);
//                outputVideoFormat.setInteger(MediaFormat.KEY_BIT_RATE, OUTPUT_VIDEO_BIT_RATE);
//                outputVideoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, OUTPUT_VIDEO_FRAME_RATE);
//                outputVideoFormat.setInteger(
//                        MediaFormat.KEY_I_FRAME_INTERVAL, OUTPUT_VIDEO_IFRAME_INTERVAL);
//                if (VERBOSE) Log.d(TAG, "video format: " + outputVideoFormat);
//                // Create a MediaCodec for the desired codec, then configure it as an encoder with
//                // our desired properties. Request a Surface to use for input.
//                AtomicReference<Surface> inputSurfaceReference = new AtomicReference<Surface>();
//                videoEncoder = createVideoEncoder(
//                        videoCodecInfo, outputVideoFormat, inputSurfaceReference);
//                inputSurface = new InputSurface(inputSurfaceReference.get());
//                inputSurface.makeCurrent();
//                // 根据提取器的格式，为解码器创建一个MediaCodec。
//                outputSurface = new OutputSurfaceWithFilter(mAppContext, mFilterType);
////                outputSurface.changeFragmentShader(FRAGMENT_SHADER);
//                videoDecoder = createVideoDecoder(inputFormat, outputSurface.getSurface());
//            }
//            if (mCopyAudio) {
//                System.out.println("2223=======mCopyAudio======");
//                audioExtractor = createAudioExtractor();
//                int audioInputTrack = getAndSelectAudioTrackIndex(audioExtractor);
//                LogUtils.e("====22222==2===="+audioInputTrack);
//                assertTrue("missing audio track in test video", audioInputTrack != -1);
//                MediaFormat inputFormat = audioExtractor.getTrackFormat(audioInputTrack);
//                MediaFormat outputAudioFormat =
//                        MediaFormat.createAudioFormat(
//                                OUTPUT_AUDIO_MIME_TYPE, OUTPUT_AUDIO_SAMPLE_RATE_HZ,
//                                OUTPUT_AUDIO_CHANNEL_COUNT);
//
//                outputAudioFormat.setInteger(MediaFormat.KEY_BIT_RATE, OUTPUT_AUDIO_BIT_RATE);
//                outputAudioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, OUTPUT_AUDIO_AAC_PROFILE);
//                // 为所需的编解码器创建一个MediaCodec，然后将其配置为具有我们所需属性的编码器。请求一个表面用于输入。
//                audioEncoder = createAudioEncoder(audioCodecInfo, outputAudioFormat);
//                // 根据提取器的格式，为解码器创建一个MediaCodec。
//                audioDecoder = createAudioDecoder(inputFormat);
//            }
//            // 创建一个muxer，但是还没有启动或添加跟踪。
//            muxer = createMuxer();
//            doExtractDecodeEditEncodeMux(
//                    videoExtractor,
//                    audioExtractor,
//                    videoDecoder,
//                    videoEncoder,
//                    audioDecoder,
//                    audioEncoder,
//                    muxer,
//                    inputSurface,
//                    outputSurface);
//        } finally {
//            if (VERBOSE) Log.d(TAG, "releasing extractor, decoder, encoder, and muxer");
//            // Try to release everything we acquired, even if one of the releases fails, in which
//            // case we save the first exception we got and re-throw at the end (unless something
//            // other exception has already been thrown). This guarantees the first exception thrown
//            // is reported as the cause of the error, everything is (attempted) to be released, and
//            // all other exceptions appear in the logs.
//            try {
//                if (videoExtractor != null) {
//                    videoExtractor.release();
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "error while releasing videoExtractor", e);
//                if (exception == null) {
//                    exception = e;
//                }
//            }
//            try {
//                if (audioExtractor != null) {
//                    audioExtractor.release();
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "error while releasing audioExtractor", e);
//                if (exception == null) {
//                    exception = e;
//                }
//            }
//            try {
//                if (videoDecoder != null) {
//                    videoDecoder.stop();
//                    videoDecoder.release();
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "error while releasing videoDecoder", e);
//                if (exception == null) {
//                    exception = e;
//                }
//            }
//            try {
//                if (outputSurface != null) {
//                    outputSurface.release();
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "error while releasing outputSurface", e);
//                if (exception == null) {
//                    exception = e;
//                }
//            }
//            try {
//                if (videoEncoder != null) {
//                    videoEncoder.stop();
//                    videoEncoder.release();
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "error while releasing videoEncoder", e);
//                if (exception == null) {
//                    exception = e;
//                }
//            }
//            try {
//                if (audioDecoder != null) {
//                    audioDecoder.stop();
//                    audioDecoder.release();
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "error while releasing audioDecoder", e);
//                if (exception == null) {
//                    exception = e;
//                }
//            }
//            try {
//                if (audioEncoder != null) {
//                    audioEncoder.stop();
//                    audioEncoder.release();
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "error while releasing audioEncoder", e);
//                if (exception == null) {
//                    exception = e;
//                }
//            }
//            try {
//                if (muxer != null) {
//                    muxer.stop();
//                    muxer.release();
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "error while releasing muxer", e);
//                if (exception == null) {
//                    exception = e;
//                }
//            }
//            try {
//                if (inputSurface != null) {
//                    inputSurface.release();
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "error while releasing inputSurface", e);
//                if (exception == null) {
//                    exception = e;
//                }
//            }
//        }
//        if (exception != null) {
//            throw exception;
//        }
//    }
//
//    /**
//     * Creates an extractor that reads its frames from {@link #mSourceResId}.视频分离
//     */
//    private MediaExtractor createExtractor() throws IOException {
//        MediaExtractor extractor;
////        AssetFileDescriptor srcFd = getContext().getResources().openRawResourceFd(mSourceResId);
////        extractor = new MediaExtractor();
////        extractor.setDataSource(srcFd.getFileDescriptor(), srcFd.getStartOffset(),
////                srcFd.getLength());
//        extractor = new MediaExtractor();//音视频的分离器
//        extractor.setDataSource(mPath);
//
//
//
//
//        return extractor;
//    }
//    //音频分离
//    private MediaExtractor createAudioExtractor() throws IOException {
//        MediaExtractor extractor;
////        AssetFileDescriptor srcFd = getContext().getResources().openRawResourceFd(mSourceResId);
////        extractor = new MediaExtractor();
////        extractor.setDataSource(srcFd.getFileDescriptor(), srcFd.getStartOffset(),
////                srcFd.getLength());
//        extractor = new MediaExtractor();//音视频的分离器
////        int audioTrack = -1;
////        boolean hasAudio = false;
////
//            extractor.setDataSource(mPathAudio);
////            for (int i = 0; i < extractor.getTrackCount(); i++) {
////                MediaFormat trackFormat = extractor.getTrackFormat(i);
////                String mime = trackFormat.getString(MediaFormat.KEY_MIME);
////                if (mime.startsWith("audio/")) {
////                    audioTrack = i;
////                    hasAudio = true;
////                    break;
////                }
////            }
////        if (hasAudio) {
////            extractor.selectTrack(audioTrack);
////
////        }
//
//
//        return extractor;
//    }
//
//    /**
//     * Creates a decoder for the given format, which outputs to the given surface.
//     *
//     * @param inputFormat the format of the stream to decode
//     * @param surface     into which to decode the frames
//     */
//    private MediaCodec createVideoDecoder(MediaFormat inputFormat, Surface surface) throws IOException {
//        MediaCodec decoder = MediaCodec.createDecoderByType(getMimeTypeFor(inputFormat));
//        decoder.configure(inputFormat, surface, null, 0);
//        decoder.start();
//        return decoder;
//    }
//
//    /**
//     * Creates an encoder for the given format using the specified codec, taking input from a
//     * surface.
//     * <p>
//     * <p>The surface to use as input is stored in the given reference.
//     *
//     * @param codecInfo        of the codec to use
//     * @param format           of the stream to be produced
//     * @param surfaceReference to store the surface to use as input
//     */
//    private MediaCodec createVideoEncoder(
//            MediaCodecInfo codecInfo,
//            MediaFormat format,
//            AtomicReference<Surface> surfaceReference) throws IOException {
//        MediaCodec encoder = MediaCodec.createByCodecName(codecInfo.getName());
//        encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
//        // Must be called before start() is.
//        surfaceReference.set(encoder.createInputSurface());
//        encoder.start();
//        return encoder;
//    }
//
//    /**
//     * Creates a decoder for the given format.
//     *
//     * @param inputFormat the format of the stream to decode
//     */
//    private MediaCodec createAudioDecoder(MediaFormat inputFormat) throws IOException {
//        MediaCodec decoder = MediaCodec.createDecoderByType(getMimeTypeFor(inputFormat));
//        decoder.configure(inputFormat, null, null, 0);
//        decoder.start();
//        return decoder;
//    }
//
//    /**
//     * Creates an encoder for the given format using the specified codec.
//     *
//     * @param codecInfo of the codec to use
//     * @param format    of the stream to be produced
//     */
//    private MediaCodec createAudioEncoder(MediaCodecInfo codecInfo, MediaFormat format) throws IOException {
//        MediaCodec encoder = MediaCodec.createByCodecName(codecInfo.getName());
//        encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
//        encoder.start();
//        return encoder;
//    }
//
//    /**
//     * Creates a muxer to write the encoded frames.
//     * <p>
//     * <p>The muxer is not started as it needs to be started only after all streams have been added.
//     */
//    private MediaMuxer createMuxer() throws IOException {
//        //MediaMuxer  音视频的混合器
////        MediaMuxer muxer=new MediaMuxer(mOutputFile, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
//
////        MediaFormat trackFormat = extractor.getTrackFormat(audioTrack);
////        int writeAudioIndex = muxer.addTrack(trackFormat);
////        muxer.start();
////        ByteBuffer byteBuffer = ByteBuffer.allocate(trackFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE));
////        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
//
//        return new MediaMuxer(mOutputFile, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
//    }
//
//    private int getAndSelectVideoTrackIndex(MediaExtractor extractor) {
//        for (int index = 0; index < extractor.getTrackCount(); ++index) {
//            if (VERBOSE) {
//                Log.d(TAG, "format for track " + index + " is "
//                        + getMimeTypeFor(extractor.getTrackFormat(index)));
//            }
//            if (isVideoFormat(extractor.getTrackFormat(index))) {
//                extractor.selectTrack(index);
//                return index;
//            }
//        }
//        return -1;
//    }
//
//    private int getAndSelectAudioTrackIndex(MediaExtractor extractor) {
//        LogUtils.e("==========="+extractor.getTrackCount());
//        MediaFormat audioFormat = null;
//        for (int index = 0; index < extractor.getTrackCount(); ++index) {
////            if (VERBOSE) {
////                Log.d(TAG, "format for track " + index + " is "
////                        + getMimeTypeFor(extractor.getTrackFormat(index)));
////            }
////            if (isAudioFormat(extractor.getTrackFormat(index))) {
////                extractor.selectTrack(index);
////                LogUtils.e("==========="+index);
////                return index;
////            }
//
//            audioFormat = extractor.getTrackFormat(index);
//            String mimeType = audioFormat.getString(MediaFormat.KEY_MIME);
//            if (mimeType.startsWith("audio/")) {
//                extractor.selectTrack(index);
//                LogUtils.e("==========="+index);
//                return index;
//            }
//
//
//
//
//        }
//        return -1;
//    }
//
//    /**
//     * Does the actual work for extracting, decoding, encoding and muxing.
//     */
//    private void doExtractDecodeEditEncodeMux(
//            MediaExtractor videoExtractor,
//            MediaExtractor audioExtractor,
//            MediaCodec videoDecoder,
//            MediaCodec videoEncoder,
//            MediaCodec audioDecoder,
//            MediaCodec audioEncoder,
//            MediaMuxer muxer,
//            InputSurface inputSurface,
//            OutputSurfaceWithFilter outputSurface) {
//        ByteBuffer[] videoDecoderInputBuffers = null;
//        ByteBuffer[] videoDecoderOutputBuffers = null;
//        ByteBuffer[] videoEncoderOutputBuffers = null;
//        MediaCodec.BufferInfo videoDecoderOutputBufferInfo = null;
//        MediaCodec.BufferInfo videoEncoderOutputBufferInfo = null;
//        if (mCopyVideo) {
//            videoDecoderInputBuffers = videoDecoder.getInputBuffers();
//            videoDecoderOutputBuffers = videoDecoder.getOutputBuffers();
//            videoEncoderOutputBuffers = videoEncoder.getOutputBuffers();
//            videoDecoderOutputBufferInfo = new MediaCodec.BufferInfo();
//            videoEncoderOutputBufferInfo = new MediaCodec.BufferInfo();
//        }
//        ByteBuffer[] audioDecoderInputBuffers = null;
//        ByteBuffer[] audioDecoderOutputBuffers = null;
//        ByteBuffer[] audioEncoderInputBuffers = null;
//        ByteBuffer[] audioEncoderOutputBuffers = null;
//        MediaCodec.BufferInfo audioDecoderOutputBufferInfo = null;
//        MediaCodec.BufferInfo audioEncoderOutputBufferInfo = null;
//        if (mCopyAudio) {
//            audioDecoderInputBuffers = audioDecoder.getInputBuffers();
//            audioDecoderOutputBuffers = audioDecoder.getOutputBuffers();
//            audioEncoderInputBuffers = audioEncoder.getInputBuffers();
//            audioEncoderOutputBuffers = audioEncoder.getOutputBuffers();
//            audioDecoderOutputBufferInfo = new MediaCodec.BufferInfo();
//            audioEncoderOutputBufferInfo = new MediaCodec.BufferInfo();
//        }
//        // We will get these from the decoders when notified of a format change.
//        MediaFormat decoderOutputVideoFormat = null;
//        MediaFormat decoderOutputAudioFormat = null;
//        // We will get these from the encoders when notified of a format change.
//        MediaFormat encoderOutputVideoFormat = null;
//        MediaFormat encoderOutputAudioFormat = null;
//        // We will determine these once we have the output format.
//        int outputVideoTrack = -1;
//        int outputAudioTrack = -1;
//        // Whether things are done on the video side.
//        boolean videoExtractorDone = false;
//        boolean videoDecoderDone = false;
//        boolean videoEncoderDone = false;
//        // Whether things are done on the audio side.
//        boolean audioExtractorDone = false;
//        boolean audioDecoderDone = false;
//        boolean audioEncoderDone = false;
//        // The audio decoder output buffer to process, -1 if none.
//        int pendingAudioDecoderOutputBufferIndex = -1;
//        boolean muxing = false;
//        int videoExtractedFrameCount = 0;
//        int videoDecodedFrameCount = 0;
//        int videoEncodedFrameCount = 0;
//        int audioExtractedFrameCount = 0;
//        int audioDecodedFrameCount = 0;
//        int audioEncodedFrameCount = 0;
//        while ((mCopyVideo && !videoEncoderDone) || (mCopyAudio && !audioEncoderDone)) {
//            if (VERBOSE) {
//                Log.d(TAG, String.format(
//                        "loop: "
//                                + "V(%b){"
//                                + "extracted:%d(done:%b) "
//                                + "decoded:%d(done:%b) "
//                                + "encoded:%d(done:%b)} "
//                                + "A(%b){"
//                                + "extracted:%d(done:%b) "
//                                + "decoded:%d(done:%b) "
//                                + "encoded:%d(done:%b) "
//                                + "pending:%d} "
//                                + "muxing:%b(V:%d,A:%d)",
//                        mCopyVideo,
//                        videoExtractedFrameCount, videoExtractorDone,
//                        videoDecodedFrameCount, videoDecoderDone,
//                        videoEncodedFrameCount, videoEncoderDone,
//                        mCopyAudio,
//                        audioExtractedFrameCount, audioExtractorDone,
//                        audioDecodedFrameCount, audioDecoderDone,
//                        audioEncodedFrameCount, audioEncoderDone,
//                        pendingAudioDecoderOutputBufferIndex,
//                        muxing, outputVideoTrack, outputAudioTrack));
//            }
//            // Extract video from file and feed to decoder.
//            // Do not extract video if we have determined the output format but we are not yet
//            // ready to mux the frames.
//            while (mCopyVideo && !videoExtractorDone
//                    && (encoderOutputVideoFormat == null || muxing)) {
//                int decoderInputBufferIndex = videoDecoder.dequeueInputBuffer(TIMEOUT_USEC);
//                if (decoderInputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
//                    if (VERBOSE) Log.d(TAG, "no video decoder input buffer");
//                    break;
//                }
//                if (VERBOSE) {
//                    Log.d(TAG, "video decoder: returned input buffer: " + decoderInputBufferIndex);
//                }
//                ByteBuffer decoderInputBuffer = videoDecoderInputBuffers[decoderInputBufferIndex];
//                int size = videoExtractor.readSampleData(decoderInputBuffer, 0);
//                long presentationTime = videoExtractor.getSampleTime();
//                if (VERBOSE) {
//                    Log.d(TAG, "video extractor: returned buffer of size " + size);
//                    Log.d(TAG, "video extractor: returned buffer for time " + presentationTime);
//                }
//                if (size >= 0) {
//                    videoDecoder.queueInputBuffer(
//                            decoderInputBufferIndex,
//                            0,
//                            size,
//                            presentationTime,
//                            videoExtractor.getSampleFlags());
//                }
//                videoExtractorDone = !videoExtractor.advance();
//                if (videoExtractorDone) {
//                    if (VERBOSE) Log.d(TAG, "video extractor: EOS");
//                    videoDecoder.queueInputBuffer(
//                            decoderInputBufferIndex,
//                            0,
//                            0,
//                            0,
//                            MediaCodec.BUFFER_FLAG_END_OF_STREAM);
//                }
//                videoExtractedFrameCount++;
//                // We extracted a frame, let's try something else next.
//                break;
//            }
//            // Extract audio from file and feed to decoder.
//            // Do not extract audio if we have determined the output format but we are not yet
//            // ready to mux the frames.
//            while (mCopyAudio && !audioExtractorDone
//                    && (encoderOutputAudioFormat == null || muxing)) {
//                int decoderInputBufferIndex = audioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
//                if (decoderInputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
//                    if (VERBOSE) Log.d(TAG, "no audio decoder input buffer");
//                    break;
//                }
//                if (VERBOSE) {
//                    Log.d(TAG, "audio decoder: returned input buffer: " + decoderInputBufferIndex);
//                }
//                ByteBuffer decoderInputBuffer = audioDecoderInputBuffers[decoderInputBufferIndex];
//                int size = audioExtractor.readSampleData(decoderInputBuffer, 0);
//                long presentationTime = audioExtractor.getSampleTime();
//                if (VERBOSE) {
//                    Log.d(TAG, "audio extractor: returned buffer of size " + size);
//                    Log.d(TAG, "audio extractor: returned buffer for time " + presentationTime);
//                }
//                if (size >= 0) {
//                    audioDecoder.queueInputBuffer(
//                            decoderInputBufferIndex,
//                            0,
//                            size,
//                            presentationTime,
//                            audioExtractor.getSampleFlags());
//                }
//                audioExtractorDone = !audioExtractor.advance();
//                if (audioExtractorDone) {
//                    if (VERBOSE) Log.d(TAG, "audio extractor: EOS");
//                    audioDecoder.queueInputBuffer(
//                            decoderInputBufferIndex,
//                            0,
//                            0,
//                            0,
//                            MediaCodec.BUFFER_FLAG_END_OF_STREAM);
//                }
//                audioExtractedFrameCount++;
//                // We extracted a frame, let's try something else next.
//                break;
//            }
//            // Poll output frames from the video decoder and feed the encoder.
//            while (mCopyVideo && !videoDecoderDone
//                    && (encoderOutputVideoFormat == null || muxing)) {
//                int decoderOutputBufferIndex =
//                        videoDecoder.dequeueOutputBuffer(
//                                videoDecoderOutputBufferInfo, TIMEOUT_USEC);
//                if (decoderOutputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
//                    if (VERBOSE) Log.d(TAG, "no video decoder output buffer");
//                    break;
//                }
//                if (decoderOutputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
//                    if (VERBOSE) Log.d(TAG, "video decoder: output buffers changed");
//                    videoDecoderOutputBuffers = videoDecoder.getOutputBuffers();
//                    break;
//                }
//                if (decoderOutputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//                    decoderOutputVideoFormat = videoDecoder.getOutputFormat();
//                    if (VERBOSE) {
//                        Log.d(TAG, "video decoder: output format changed: "
//                                + decoderOutputVideoFormat);
//                    }
//                    break;
//                }
//                if (VERBOSE) {
//                    Log.d(TAG, "video decoder: returned output buffer: "
//                            + decoderOutputBufferIndex);
//                    Log.d(TAG, "video decoder: returned buffer of size "
//                            + videoDecoderOutputBufferInfo.size);
//                }
//                ByteBuffer decoderOutputBuffer =
//                        videoDecoderOutputBuffers[decoderOutputBufferIndex];
//                if ((videoDecoderOutputBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG)
//                        != 0) {
//                    if (VERBOSE) Log.d(TAG, "video decoder: codec config buffer");
//                    videoDecoder.releaseOutputBuffer(decoderOutputBufferIndex, false);
//                    break;
//                }
//                if (VERBOSE) {
//                    Log.d(TAG, "video decoder: returned buffer for time "
//                            + videoDecoderOutputBufferInfo.presentationTimeUs);
//                }
//                boolean render = videoDecoderOutputBufferInfo.size != 0;
//                videoDecoder.releaseOutputBuffer(decoderOutputBufferIndex, render);
//                if (render) {
//                    if (VERBOSE) Log.d(TAG, "output surface: await new image");
//                    outputSurface.awaitNewImage();
//                    // Edit the frame and send it to the encoder.
//                    if (VERBOSE) Log.d(TAG, "output surface: draw image");
//                    outputSurface.drawImage();
//                    inputSurface.setPresentationTime(
//                            videoDecoderOutputBufferInfo.presentationTimeUs * 1000);
//                    if (VERBOSE) Log.d(TAG, "input surface: swap buffers");
//                    inputSurface.swapBuffers();
//                    if (VERBOSE) Log.d(TAG, "video encoder: notified of new frame");
//                }
//                if ((videoDecoderOutputBufferInfo.flags
//                        & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
//                    if (VERBOSE) Log.d(TAG, "video decoder: EOS");
//                    videoDecoderDone = true;
//                    videoEncoder.signalEndOfInputStream();
//                }
//                videoDecodedFrameCount++;
//                // We extracted a pending frame, let's try something else next.
//                break;
//            }
//            // Poll output frames from the audio decoder.
//            // Do not poll if we already have a pending buffer to feed to the encoder.
//            while (mCopyAudio && !audioDecoderDone && pendingAudioDecoderOutputBufferIndex == -1
//                    && (encoderOutputAudioFormat == null || muxing)) {
//                int decoderOutputBufferIndex =
//                        audioDecoder.dequeueOutputBuffer(
//                                audioDecoderOutputBufferInfo, TIMEOUT_USEC);
//                if (decoderOutputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
//                    if (VERBOSE) Log.d(TAG, "no audio decoder output buffer");
//                    break;
//                }
//                if (decoderOutputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
//                    if (VERBOSE) Log.d(TAG, "audio decoder: output buffers changed");
//                    audioDecoderOutputBuffers = audioDecoder.getOutputBuffers();
//                    break;
//                }
//                if (decoderOutputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//                    decoderOutputAudioFormat = audioDecoder.getOutputFormat();
//                    if (VERBOSE) {
//                        Log.d(TAG, "audio decoder: output format changed: "
//                                + decoderOutputAudioFormat);
//                    }
//                    break;
//                }
//                if (VERBOSE) {
//                    Log.d(TAG, "audio decoder: returned output buffer: "
//                            + decoderOutputBufferIndex);
//                }
//                if (VERBOSE) {
//                    Log.d(TAG, "audio decoder: returned buffer of size "
//                            + audioDecoderOutputBufferInfo.size);
//                }
//                ByteBuffer decoderOutputBuffer =
//                        audioDecoderOutputBuffers[decoderOutputBufferIndex];
//                if ((audioDecoderOutputBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG)
//                        != 0) {
//                    if (VERBOSE) Log.d(TAG, "audio decoder: codec config buffer");
//                    audioDecoder.releaseOutputBuffer(decoderOutputBufferIndex, false);
//                    break;
//                }
//                if (VERBOSE) {
//                    Log.d(TAG, "audio decoder: returned buffer for time "
//                            + audioDecoderOutputBufferInfo.presentationTimeUs);
//                }
//                if (VERBOSE) {
//                    Log.d(TAG, "audio decoder: output buffer is now pending: "
//                            + pendingAudioDecoderOutputBufferIndex);
//                }
//                pendingAudioDecoderOutputBufferIndex = decoderOutputBufferIndex;
//                audioDecodedFrameCount++;
//                // We extracted a pending frame, let's try something else next.
//                break;
//            }
//            // Feed the pending decoded audio buffer to the audio encoder.
//            while (mCopyAudio && pendingAudioDecoderOutputBufferIndex != -1) {
//                if (VERBOSE) {
//                    Log.d(TAG, "audio decoder: attempting to process pending buffer: "
//                            + pendingAudioDecoderOutputBufferIndex);
//                }
//                int encoderInputBufferIndex = audioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
//                if (encoderInputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
//                    if (VERBOSE) Log.d(TAG, "no audio encoder input buffer");
//                    break;
//                }
//                if (VERBOSE) {
//                    Log.d(TAG, "audio encoder: returned input buffer: " + encoderInputBufferIndex);
//                }
//                ByteBuffer encoderInputBuffer = audioEncoderInputBuffers[encoderInputBufferIndex];
//                int size = audioDecoderOutputBufferInfo.size;
//                long presentationTime = audioDecoderOutputBufferInfo.presentationTimeUs;
//                if (VERBOSE) {
//                    Log.d(TAG, "audio decoder: processing pending buffer: "
//                            + pendingAudioDecoderOutputBufferIndex);
//                }
//                if (VERBOSE) {
//                    Log.d(TAG, "audio decoder: pending buffer of size " + size);
//                    Log.d(TAG, "audio decoder: pending buffer for time " + presentationTime);
//                }
//                if (size >= 0) {
//                    ByteBuffer decoderOutputBuffer =
//                            audioDecoderOutputBuffers[pendingAudioDecoderOutputBufferIndex]
//                                    .duplicate();
//                    decoderOutputBuffer.position(audioDecoderOutputBufferInfo.offset);
//                    decoderOutputBuffer.limit(audioDecoderOutputBufferInfo.offset + size);
//                    encoderInputBuffer.position(0);
//                    encoderInputBuffer.put(decoderOutputBuffer);
//                    audioEncoder.queueInputBuffer(
//                            encoderInputBufferIndex,
//                            0,
//                            size,
//                            presentationTime,
//                            audioDecoderOutputBufferInfo.flags);
//                }
//                audioDecoder.releaseOutputBuffer(pendingAudioDecoderOutputBufferIndex, false);
//                pendingAudioDecoderOutputBufferIndex = -1;
//                if ((audioDecoderOutputBufferInfo.flags
//                        & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
//                    if (VERBOSE) Log.d(TAG, "audio decoder: EOS");
//                    audioDecoderDone = true;
//                }
//                // We enqueued a pending frame, let's try something else next.
//                //我们排队等待一个挂起的框架，接下来我们再试试别的。
//                break;
//            }
//            // Poll frames from the video encoder and send them to the muxer.
//            //来自视频编码器的投票帧并将它们发送到muxer。
//            while (mCopyVideo && !videoEncoderDone
//                    && (encoderOutputVideoFormat == null || muxing)) {
//                int encoderOutputBufferIndex = videoEncoder.dequeueOutputBuffer(
//                        videoEncoderOutputBufferInfo, TIMEOUT_USEC);
//                if (encoderOutputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
//                    if (VERBOSE) Log.d(TAG, "no video encoder output buffer");
//                    break;
//                }
//                if (encoderOutputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
//                    if (VERBOSE) Log.d(TAG, "video encoder: output buffers changed");
//                    videoEncoderOutputBuffers = videoEncoder.getOutputBuffers();
//                    break;
//                }
//                if (encoderOutputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//                    if (VERBOSE) Log.d(TAG, "video encoder: output format changed");
//                    if (outputVideoTrack >= 0) {
//                        fail("video encoder changed its output format again?");
//                    }
//                    encoderOutputVideoFormat = videoEncoder.getOutputFormat();
//                    break;
//                }
//                assertTrue("should have added track before processing output", muxing);
//                if (VERBOSE) {
//                    Log.d(TAG, "video encoder: returned output buffer: "
//                            + encoderOutputBufferIndex);
//                    Log.d(TAG, "video encoder: returned buffer of size "
//                            + videoEncoderOutputBufferInfo.size);
//                }
//                ByteBuffer encoderOutputBuffer =
//                        videoEncoderOutputBuffers[encoderOutputBufferIndex];
//                if ((videoEncoderOutputBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG)
//                        != 0) {
//                    if (VERBOSE) Log.d(TAG, "video encoder: codec config buffer");
//                    // Simply ignore codec config buffers.
//                    videoEncoder.releaseOutputBuffer(encoderOutputBufferIndex, false);
//                    break;
//                }
//                if (VERBOSE) {
//                    Log.d(TAG, "video encoder: returned buffer for time "
//                            + videoEncoderOutputBufferInfo.presentationTimeUs);
//                }
//                if (videoEncoderOutputBufferInfo.size != 0) {
//                    muxer.writeSampleData(
//                            outputVideoTrack, encoderOutputBuffer, videoEncoderOutputBufferInfo);
//                }
//                if ((videoEncoderOutputBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM)
//                        != 0) {
//                    if (VERBOSE) Log.d(TAG, "video encoder: EOS");
//                    videoEncoderDone = true;
//                }
//                videoEncoder.releaseOutputBuffer(encoderOutputBufferIndex, false);
//                videoEncodedFrameCount++;
//                // We enqueued an encoded frame, let's try something else next.
//                break;
//            }
//            // Poll frames from the audio encoder and send them to the muxer.
//            while (mCopyAudio && !audioEncoderDone
//                    && (encoderOutputAudioFormat == null || muxing)) {
//                int encoderOutputBufferIndex = audioEncoder.dequeueOutputBuffer(
//                        audioEncoderOutputBufferInfo, TIMEOUT_USEC);
//                if (encoderOutputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
//                    if (VERBOSE) Log.d(TAG, "no audio encoder output buffer");
//                    break;
//                }
//                if (encoderOutputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
//                    if (VERBOSE) Log.d(TAG, "audio encoder: output buffers changed");
//                    audioEncoderOutputBuffers = audioEncoder.getOutputBuffers();
//                    break;
//                }
//                if (encoderOutputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//                    if (VERBOSE) Log.d(TAG, "audio encoder: output format changed");
//                    if (outputAudioTrack >= 0) {
//                        fail("audio encoder changed its output format again?");
//                    }
//                    encoderOutputAudioFormat = audioEncoder.getOutputFormat();
//                    break;
//                }
//                assertTrue("should have added track before processing output", muxing);
//                if (VERBOSE) {
//                    Log.d(TAG, "audio encoder: returned output buffer: "
//                            + encoderOutputBufferIndex);
//                    Log.d(TAG, "audio encoder: returned buffer of size "
//                            + audioEncoderOutputBufferInfo.size);
//                }
//                ByteBuffer encoderOutputBuffer =
//                        audioEncoderOutputBuffers[encoderOutputBufferIndex];
//                if ((audioEncoderOutputBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG)
//                        != 0) {
//                    if (VERBOSE) Log.d(TAG, "audio encoder: codec config buffer");
//                    // Simply ignore codec config buffers.
//                    audioEncoder.releaseOutputBuffer(encoderOutputBufferIndex, false);
//                    break;
//                }
//                if (VERBOSE) {
//                    Log.d(TAG, "audio encoder: returned buffer for time "
//                            + audioEncoderOutputBufferInfo.presentationTimeUs);
//                }
//                if (audioEncoderOutputBufferInfo.size != 0) {
//                    muxer.writeSampleData(
//                            outputAudioTrack, encoderOutputBuffer, audioEncoderOutputBufferInfo);
//                }
//                if ((audioEncoderOutputBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM)
//                        != 0) {
//                    if (VERBOSE) Log.d(TAG, "audio encoder: EOS");
//                    audioEncoderDone = true;
//                }
//                audioEncoder.releaseOutputBuffer(encoderOutputBufferIndex, false);
//                audioEncodedFrameCount++;
//                // We enqueued an encoded frame, let's try something else next.
//                break;
//            }
//            if (!muxing
//                    && (!mCopyAudio || encoderOutputAudioFormat != null)
//                    && (!mCopyVideo || encoderOutputVideoFormat != null)) {
//                if (mCopyVideo) {
//                    Log.d(TAG, "muxer: adding video track.");
//                    outputVideoTrack = muxer.addTrack(encoderOutputVideoFormat);
//                }
//                if (mCopyAudio) {
//                    Log.d(TAG, "muxer: adding audio track.");
//                    outputAudioTrack = muxer.addTrack(encoderOutputAudioFormat);
//                }
//                Log.d(TAG, "muxer: starting");
//                muxer.start();
//                muxing = true;
//            }
//        }
//        // Basic sanity checks.
//        if (mCopyVideo) {
//            assertEquals("encoded and decoded video frame counts should match",
//                    videoDecodedFrameCount, videoEncodedFrameCount);
//            assertTrue("decoded frame count should be less than extracted frame count",
//                    videoDecodedFrameCount <= videoExtractedFrameCount);
//        }
//        if (mCopyAudio) {
//            assertEquals("no frame should be pending", -1, pendingAudioDecoderOutputBufferIndex);
//        }
//        // TODO: Check the generated output file.
//
//        System.out.println("=========保存完成=============");
////        VideoFilterActivity.setstart();//完成跳转
//
//    }
//
//    private static boolean isVideoFormat(MediaFormat format) {
//        return getMimeTypeFor(format).startsWith("video/");
//    }
//
//    private static boolean isAudioFormat(MediaFormat format) {
//        return getMimeTypeFor(format).startsWith("audio/");
//    }
//
//    private static String getMimeTypeFor(MediaFormat format) {
//        return format.getString(MediaFormat.KEY_MIME);
//    }
//
//    /**
//     * Returns the first codec capable of encoding the specified MIME type, or null if no match was
//     * found.
//     */
//    private static MediaCodecInfo selectCodec(String mimeType) {
//        int numCodecs = MediaCodecList.getCodecCount();
//        for (int i = 0; i < numCodecs; i++) {
//            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
//            if (!codecInfo.isEncoder()) {
//                continue;
//            }
//            String[] types = codecInfo.getSupportedTypes();
//            for (int j = 0; j < types.length; j++) {
//                if (types[j].equalsIgnoreCase(mimeType)) {
//                    return codecInfo;
//                }
//            }
//        }
//        return null;
//    }
//
//}