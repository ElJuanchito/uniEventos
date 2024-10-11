package co.edu.uniquindio.uni_eventos.controllers;

import co.edu.uniquindio.uni_eventos.dtos.MessageDTO;
import co.edu.uniquindio.uni_eventos.dtos.event.CreateEventDTO;
import co.edu.uniquindio.uni_eventos.dtos.event.EventInfoDTO;
import co.edu.uniquindio.uni_eventos.dtos.event.FilterEventDTO;
import co.edu.uniquindio.uni_eventos.dtos.event.UpdateEventDTO;
import co.edu.uniquindio.uni_eventos.services.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @PostMapping("/create-event")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO<String> createEvent(@Valid @RequestBody CreateEventDTO eventDTO) throws Exception{
        eventService.createEvent(eventDTO);
        return new MessageDTO<>(false, "Event created");
    }

    @PutMapping("/update-event")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO<String> updateEvent(@Valid @RequestBody UpdateEventDTO eventDTO) throws Exception{
        eventService.updateEvent(eventDTO);
        return new MessageDTO<>(false, "Event updated");
    }

    @DeleteMapping("delete-event")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO<String> deleteEvent(@NotBlank @PathVariable String id) throws Exception{
        eventService.deleteEvent(id);
        return new MessageDTO<>(false, "Event deleted");
    }

    @GetMapping("/get/event/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO<EventInfoDTO> getEvent(@NotBlank @PathVariable String id) throws Exception{
        EventInfoDTO eventDTO = eventService.getEvent(id);
        return new MessageDTO<>(false, eventDTO);
    }

    @GetMapping("/get/all-events")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO<List<EventInfoDTO>> getAllEvents() throws Exception{
        List<EventInfoDTO> events = eventService.getAllEvents();
        return new MessageDTO<>(false, events);
    }

    @GetMapping("/get/filter-events")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO<List<EventInfoDTO>> filterEvents(@RequestBody FilterEventDTO filterEventDTO) throws Exception{
        List<EventInfoDTO> events = eventService.filterEvents(filterEventDTO);
        return new MessageDTO<>(false, events);
    }
}
