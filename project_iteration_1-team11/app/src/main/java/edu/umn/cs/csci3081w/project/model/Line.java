package edu.umn.cs.csci3081w.project.model;

public class Line {
   private Route outboundRoute;
   private Route inboundRoute;

   public Line(Route inboundRoute, Route outboundRoute) {
      this.inboundRoute = inboundRoute;
      this.outboundRoute = outboundRoute;
   }

   public Route getOutboundRoute() {
      return outboundRoute;
   }

   public Route getInboundRoute() {
      return inboundRoute;
   }

   public void setInboundRoute(Route inboundRoute) {
      this.inboundRoute = inboundRoute;
   }

   public void setOutboundRoute(Route outboundRoute) {
      this.outboundRoute = outboundRoute;
   }
}
