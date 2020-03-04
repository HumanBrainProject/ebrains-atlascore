package de.fzj.atlascore.referencespace;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to communicate with all kind of repository sources, to get referencespaces and details
 *
 * {@link ReferencespaceRepository}
 *
 * @author Vadim Marcenko
 */
@Service
public class ReferencespaceService {

    private final ReferencespaceRepository referencespaceRepository;

    public ReferencespaceService(ReferencespaceRepository referencespaceRepository) {
        this.referencespaceRepository = referencespaceRepository;
    }

    public List<Referencespace> getReferencespaces() {
        return this.referencespaceRepository.getAllReferencespacesWithProperties();
//        return referencespaceRepository.findAll();
    }

    public Referencespace getReferencespaceByName(String name) {
        return referencespaceRepository.findOneByName(name);
    }
}
