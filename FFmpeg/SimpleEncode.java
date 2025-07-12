/**
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import java.nio.file.Path;

import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.Input;
import com.github.kokorin.jaffree.ffmpeg.Output;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;

/**
 * @author Mark Himsley
 */

public class SimpleEncode implements Runnable {

    public static void main(final String[] args) {
        final Runnable app = new SimpleEncode();
        app.run();
    }

    @Override
    public void run() {
        FFmpeg.atPath(Path.of("/home/user/bin/"))
                .addArguments("-loglevel", "debug")
                .setOverwriteOutput(true)
                .addInput(getInput())
                .addOutput(getOutputVideo())
                .addOutput(getOutputAudio())
                .execute();
    }

    private Input getInput() {
        return UrlInput.fromUrl("input.mp4")
                .setDuration(5000);
    }

    private Output getOutputVideo() {
        return UrlOutput.toUrl("output.mov")
                .setCodec("v", "libx264")
                .addArguments("-profile:v", "main")
                .addArguments("-level:v", "4.1")
                .addArguments("-crf:v", "18")
                .setCodec("a", "pcm_s16le")
                .addArguments("-ar:a", "48000")
                .addMap(0, "v:0")
                .addMap(0, "a:0");
     }

    private Output getOutputAudio() {
        return UrlOutput.toUrl("output.wav")
                .disableStream(StreamType.VIDEO)
                .setCodec("a", "pcm_s16le")
                .addArguments("-ar:a", "44100")
                .addMap(0, "a:0");
    }

}
