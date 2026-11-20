package ru.edu.pr03;
import java.util.*;import java.time.*;
public class BusinessTripService {
 private final List<BusinessTrip> trips=new ArrayList<>();
 public BusinessTrip create(String id,LocalDate start,LocalDate end){BusinessTrip t=new BusinessTrip(id,start,end);trips.add(t);return t;}
 public List<BusinessTrip> findByStatus(TripStatus status){return trips.stream().filter(t->t.getStatus()==status).toList();}
 // Exact contract of variant 21: compareTo, not elapsed calendar days.
 public int addDaysUntilStart(BusinessTrip trip,LocalDate today){return trip.getStart().compareTo(today);}
}