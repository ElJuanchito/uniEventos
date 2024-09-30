package co.edu.uniquindio.uni_eventos.mappers;

import co.edu.uniquindio.uni_eventos.dtos.CreateSectionDTO;
import co.edu.uniquindio.uni_eventos.entities.Section;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SectionMapper {

    Section toSection(CreateSectionDTO createSectionDTO);
}
