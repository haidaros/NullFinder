package ch.unibe.scg.nullfinder.batch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.scg.nullfinder.jpa.entity.BadCompilationUnit;
import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;
import ch.unibe.scg.nullfinder.jpa.repository.BadCompilationUnitRepository;

@Component
public class NullFinderProcessListener implements
		ItemProcessListener<Path, CompilationUnit> {

	@Autowired
	protected BadCompilationUnitRepository badCompilationUnitRepository;

	@Override
	public void beforeProcess(Path javaSourcePath) {
		// noop
	}

	@Override
	public void afterProcess(Path javaSourcePath,
			CompilationUnit compilationUnit) {
		// noop
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void onProcessError(Path javaSourcePath, Exception exception) {
		String source = null;
		try {
			source = new String(Files.readAllBytes(javaSourcePath));
		} catch (IOException ioException) {
			// noop
		}
		String exceptionString = exception.toString();
		BadCompilationUnit badCompilationUnit = new BadCompilationUnit(
				javaSourcePath, source, exceptionString);
		this.badCompilationUnitRepository.save(badCompilationUnit);
	}

}
