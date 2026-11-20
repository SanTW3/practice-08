package ru.edu.pr03;
import java.time.*;
public class BusinessTrip {
 public static final int MIN_REASON_LENGTH=10;
 private final String id; private final LocalDate start,end;
 private TripStatus status=TripStatus.DRAFT; private String rejectionReason;
 public BusinessTrip(String id,LocalDate start,LocalDate end){
 if(id==null||id.isBlank())throw new IllegalArgumentException("id is null or blank");
 if(start==null||end==null||start.isAfter(end))throw new IllegalArgumentException("start must be <= end");
 this.id=id;this.start=start;this.end=end;}
 private void require(TripStatus s){if(status!=s)throw new IllegalStateException("Invalid transition from "+status);}
 public void submit(){require(TripStatus.DRAFT);status=TripStatus.SUBMITTED;}
 public void approve(){require(TripStatus.SUBMITTED);status=TripStatus.APPROVED;}
 public void reject(String reason){require(TripStatus.SUBMITTED);if(reason==null||reason.isBlank()||reason.length()<MIN_REASON_LENGTH)throw new IllegalArgumentException("reason too short");rejectionReason=reason;status=TripStatus.REJECTED;}
 public void cancel(){cancel(LocalDate.now());}
 public void cancel(LocalDate today){if(today==null)throw new IllegalArgumentException("today is null");
 if(status!=TripStatus.DRAFT&&status!=TripStatus.SUBMITTED&&status!=TripStatus.APPROVED)throw new IllegalStateException("cannot cancel "+status);
 if(status==TripStatus.APPROVED&&today.plusDays(3).isAfter(start))throw new IllegalStateException("need at least 3 days");status=TripStatus.CANCELLED;}
 public void complete(){complete(LocalDate.now());}
 public void complete(LocalDate today){require(TripStatus.APPROVED);if(today==null)throw new IllegalArgumentException("today is null");if(today.isBefore(end))throw new IllegalStateException("complete before end");status=TripStatus.COMPLETED;}
 public String getId(){return id;} public LocalDate getStart(){return start;}public LocalDate getEnd(){return end;}
 public TripStatus getStatus(){return status;}public String getRejectionReason(){return rejectionReason;}
}