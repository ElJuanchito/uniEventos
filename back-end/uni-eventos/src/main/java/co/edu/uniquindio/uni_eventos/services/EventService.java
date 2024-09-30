package co.edu.uniquindio.uni_eventos.services;

import co.edu.uniquindio.uni_eventos.dtos.CreateEventDTO;
import co.edu.uniquindio.uni_eventos.dtos.FilterEventDTO;
import co.edu.uniquindio.uni_eventos.dtos.EventInfoDTO;
import co.edu.uniquindio.uni_eventos.dtos.UpdateEventDTO;
import co.edu.uniquindio.uni_eventos.entities.Event;
import co.edu.uniquindio.uni_eventos.exceptions.EventNotExistsException;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface EventService {

    void createEvent(CreateEventDTO eventDTO) throws Exception;
    void updateEvent(UpdateEventDTO eventDTO) throws Exception;
    void deleteEvent(String id) throws Exception;
    EventInfoDTO getEvent(String id) throws Exception;
    List<EventInfoDTO> getAllEvents() throws Exception;
    List<EventInfoDTO> filterEvents(FilterEventDTO filterEventDTO) throws Exception;
}
