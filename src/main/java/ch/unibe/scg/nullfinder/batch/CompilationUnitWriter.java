package ch.unibe.scg.nullfinder.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;
import ch.unibe.scg.nullfinder.jpa.repository.NodeRepository;

@Component
public class CompilationUnitWriter implements ItemWriter<CompilationUnit> {

	@Autowired
	protected NodeRepository nodeRepository;

	@Override
	public void write(List<? extends CompilationUnit> compilationUnits)
			throws Exception {
		for (CompilationUnit compilationUnit : compilationUnits) {
			this.nodeRepository.save(compilationUnit);
		}
	}

}
