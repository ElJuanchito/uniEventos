package co.edu.uniquindio.uni_eventos.services;

import co.edu.uniquindio.uni_eventos.dtos.CreateEventDTO;
import co.edu.uniquindio.uni_eventos.dtos.FilterEventDTO;
import co.edu.uniquindio.uni_eventos.dtos.InfoEventDTO;
import co.edu.uniquindio.uni_eventos.dtos.UpdateEventDTO;

import java.util.List;

public interface EventService {

    String createEvent(CreateEventDTO eventDTO);
    String updateEvent(UpdateEventDTO eventDTO);
    String deleteEvent(String id);
    InfoEventDTO getEvent(String id);
    List<InfoEventDTO> getAllEvents();
    String filterEvents(FilterEventDTO filterEventDTO);
}
