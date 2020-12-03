package IO;

import java.io.IOException;
import java.util.Objects;

public class FileIOManager {

	public static byte[] readFileBytes(String path) throws IOException {
		return Objects.requireNonNull(FileIOManager.class.getClassLoader().getResourceAsStream(path))
				.readAllBytes();
	}
}
