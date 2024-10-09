package co.edu.uniquindio.uni_eventos.services.impl;

import co.edu.uniquindio.uni_eventos.dtos.event.CreateEventDTO;
import co.edu.uniquindio.uni_eventos.dtos.event.FilterEventDTO;
import co.edu.uniquindio.uni_eventos.dtos.event.EventInfoDTO;
import co.edu.uniquindio.uni_eventos.dtos.event.UpdateEventDTO;
import co.edu.uniquindio.uni_eventos.entities.Event;
import co.edu.uniquindio.uni_eventos.entities.EventStatus;
import co.edu.uniquindio.uni_eventos.exceptions.EventNotExistsException;
import co.edu.uniquindio.uni_eventos.mappers.EventMapper;
import co.edu.uniquindio.uni_eventos.mappers.SectionMapper;
import co.edu.uniquindio.uni_eventos.repositories.EventRepository;
import co.edu.uniquindio.uni_eventos.services.EventService;
import co.edu.uniquindio.uni_eventos.services.ImageService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final ImageService imgService;
    private final SectionMapper sectionMapper;

    @Override
    public void createEvent(CreateEventDTO eventDTO) throws Exception{
        Event event = eventMapper.toEvent(eventDTO);

        event.setSections(eventDTO.sections().stream().map(sectionMapper::toSection).toList());
        event.setCoverImg(imgService.uploadImage(eventDTO.coverImg()));
        event.setSectionImg(imgService.uploadImage(eventDTO.sectionImg()));

        eventRepository.save(event);
    }

    @Override
    public void updateEvent(UpdateEventDTO eventDTO) throws Exception{
        Event event = getEventById(eventDTO.id());
        event = eventMapper.toUpdateEvent(eventDTO);

        imgService.deleteImage(event.getCoverImg());
        imgService.deleteImage(event.getSectionImg());

        event.setCoverImg(imgService.uploadImage(eventDTO.coverImg()));
        event.setSectionImg(imgService.uploadImage(eventDTO.sectionImg()));

        eventRepository.save(event);

    }
    @Override
    public void deleteEvent(String id) throws Exception{
        Event event = getEventById(id);
        event.setStatus(EventStatus.INACTIVE);
        eventRepository.save(event);
    }

    @Override
    public EventInfoDTO getEvent(String id) throws Exception{
        Event event = getEventById(id);
        return eventMapper.ToInfoDTO(event);
    }

    @Override
    public List<EventInfoDTO> getAllEvents() throws Exception{
        return eventRepository.findAll().stream().map(eventMapper::ToInfoDTO).toList();
    }

    @Override
    public List<EventInfoDTO> filterEvents(FilterEventDTO filterEventDTO) throws Exception{
        //TODO Econtrar una forma bonita de realizar el filtro. Salu2
        return List.of();
    }

    public Event getEventById(@NotNull String id) throws EventNotExistsException {
        Optional<Event> optionalEvent = eventRepository.findById(id);

        if(optionalEvent.isEmpty()) throw new EventNotExistsException("El evento con el id " + id + "no existe" );
        return optionalEvent.get();
    }

}
