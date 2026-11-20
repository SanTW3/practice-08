package ru.edu.pr03;
import java.util.*;import java.nio.file.*;import java.time.*;
public class SelfCheck {
 static int passed,failed;
 static void check(String name,Runnable r){try{r.run();passed++;System.out.println("PASS "+name);}catch(Throwable e){failed++;System.out.println("FAIL "+name+": "+e);}}
 static void eq(boolean b){if(!b)throw new AssertionError();}
 static void error(Class<? extends Throwable> c,Runnable r){try{r.run();}catch(Throwable e){if(c.isInstance(e))return;throw new AssertionError(e);}throw new AssertionError("exception expected");}
static LocalDate d=LocalDate.of(2026,10,10);static BusinessTrip trip(){return new BusinessTrip("T",d,d.plusDays(2));}static BusinessTrip submitted(){BusinessTrip t=trip();t.submit();return t;}static BusinessTrip approved(){BusinessTrip t=submitted();t.approve();return t;}
public static void main(String[] args)throws Exception {if(args.length>0&&args[0].equals("--interactive")){interactive();return;}
check("unit/1",()->{eq(trip().getStatus()==TripStatus.DRAFT);});
check("unit/2",()->{error(IllegalArgumentException.class,()->new BusinessTrip(null,d,d));});
check("unit/3",()->{error(IllegalArgumentException.class,()->new BusinessTrip("",d,d));});
check("unit/4",()->{error(IllegalArgumentException.class,()->new BusinessTrip("a",d.plusDays(1),d));});
check("unit/5",()->{eq(new BusinessTrip("a",d,d).getEnd().equals(d));});
check("unit/6",()->{BusinessTrip t=trip();t.submit();eq(t.getStatus()==TripStatus.SUBMITTED);});
check("unit/7",()->{BusinessTrip t=submitted();error(IllegalStateException.class,t::submit);});
check("unit/8",()->{eq(approved().getStatus()==TripStatus.APPROVED);});
check("unit/9",()->{error(IllegalStateException.class,()->trip().approve());});
check("unit/10",()->{BusinessTrip t=submitted();t.reject("1234567890");eq(t.getStatus()==TripStatus.REJECTED);});
check("unit/11",()->{error(IllegalArgumentException.class,()->submitted().reject("123"));});
check("unit/12",()->{error(IllegalStateException.class,()->trip().reject("1234567890"));});
check("unit/13",()->{BusinessTrip t=trip();t.cancel(d);eq(t.getStatus()==TripStatus.CANCELLED);});
check("unit/14",()->{BusinessTrip t=approved();t.complete(d.plusDays(2));error(IllegalStateException.class,()->t.cancel(d));});
check("unit/15",()->{BusinessTrip t=approved();t.complete(d.plusDays(2));eq(t.getStatus()==TripStatus.COMPLETED);});
check("unit/16",()->{error(IllegalStateException.class,()->submitted().complete(d.plusDays(2)));});
check("unit/17",()->{error(IllegalStateException.class,()->approved().complete(d.plusDays(1)));});
check("unit/18",()->{BusinessTrip t=submitted();t.reject("1234567890");eq(t.getRejectionReason().equals("1234567890"));});
check("unit/19",()->{BusinessTrip t=approved();t.cancel(d.minusDays(3));eq(t.getStatus()==TripStatus.CANCELLED);});
check("unit/20",()->{BusinessTrip t=approved();error(IllegalStateException.class,()->t.cancel(d.minusDays(2)));eq(t.getStatus()==TripStatus.APPROVED);});
check("unit/21",()->{eq(new BusinessTripService().addDaysUntilStart(trip(),d.minusDays(3))==3);});
check("unit/22",()->{eq(new BusinessTripService().addDaysUntilStart(trip(),d.plusDays(1))==-1);});
check("unit/23",()->{error(IllegalArgumentException.class,()->submitted().reject("123456789"));});
check("unit/24",()->{BusinessTripService s=new BusinessTripService();s.create("a",d,d);s.create("b",d,d);eq(s.findByStatus(TripStatus.DRAFT).size()==2);});
data();System.out.println("TOTAL PASS="+passed+" FAIL="+failed);if(failed>0)System.exit(1);}
static void action(BusinessTrip t,String action){for(String s:action.split(",")){String[] a=s.split(":",2);switch(a[0]){case "SUBMIT"->t.submit();case "APPROVE"->t.approve();case "REJECT"->t.reject(a.length>1?a[1]:"1234567890");case "CANCEL"->t.cancel(a.length>1?LocalDate.parse(a[1]):LocalDate.now());case "COMPLETE"->t.complete(a.length>1?LocalDate.parse(a[1]):LocalDate.now());default->throw new IllegalArgumentException("unknown action");}}}
 static void data()throws Exception {int line=0;for(String s:Files.readAllLines(Path.of("data/sample.csv"))){line++;if(s.startsWith("#")||s.startsWith("id;")||s.isBlank())continue;String[] a=s.split(";",-1);check("data/"+line,()->{String result;BusinessTrip t;try{t=new BusinessTrip(a[0],LocalDate.parse(a[1]),LocalDate.parse(a[2]));}catch(IllegalArgumentException e){eq(a[4].equals("SKIP:"+e.getMessage()));return;}try{action(t,a[3]);result="OK:status="+t.getStatus();}catch(RuntimeException e){result="ERROR:"+e.getClass().getSimpleName();}eq(result.equals(a[4]));});}}
 static void interactive(){BusinessTrip t=null;Scanner sc=new Scanner(System.in);System.out.println("create id start end | submit | approve | reject reason | cancel | complete | status | exit");while(sc.hasNextLine()){String s=sc.nextLine();if(s.isBlank()||s.equals("exit"))break;try{String[] a=s.split(" +");if(a[0].equals("create"))t=new BusinessTrip(a[1],LocalDate.parse(a[2]),LocalDate.parse(a[3]));else {if(t==null)throw new IllegalStateException("create first");if(a[0].equals("reject"))t.reject(s.substring(7));else if(!a[0].equals("status"))action(t,s.toUpperCase());}System.out.println(t.getStatus());}catch(Exception e){System.out.println(e.getClass().getSimpleName()+": "+e.getMessage());}}}
}