package org.opennms.features.poller.remote.gwt.client;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ApplicationStateTest {
	final Date m_to = new Date();
	final Date m_from = new Date(m_to.getTime() - (1000 * 60 * 60 * 24));
	final static long FIVE_MINUTES = 1000 * 60 * 5;
	static int count = 0;
	static int monitorOffset = 0;
	final static Random m_random = new Random();
	Map<String,GWTApplication> m_applications = null;
	Map<String,GWTMonitoredService> m_services = null;
	Map<String,GWTLocationMonitor> m_monitors = null;

	@Before
	public void setup() {
		m_applications = new HashMap<String,GWTApplication>();
		m_services = new HashMap<String,GWTMonitoredService>();
		m_monitors = new HashMap<String,GWTLocationMonitor>();
	}

	@Test
	public void testApplicationStatusUnknown() {
		GWTApplicationStatus appStatus = new GWTApplicationStatus(getApplication("TestApp1"), m_from, m_to, null);
		assertEquals(Status.UNKNOWN, appStatus.getStatus());
	}

	@Test
	public void testApplicationStatusUp() {
		GWTApplicationStatus status = getUpApplicationStatus("TestApp1");
		
		assertEquals(Status.UP, status.getStatus());
		assertEquals(Double.valueOf(100.0), status.getAvailability());
	}

	@Test
	public void testApplicationStatusMarginal() {
		GWTApplicationStatus status = getMarginalApplicationStatus("TestApp1");
		
		assertEquals(Status.MARGINAL, status.getStatus());
	}

	@Test
	public void testApplicationStatusDown() {
		GWTApplicationStatus status = getDownApplicationStatus("TestApp1");
		
		assertEquals(Status.DOWN, status.getStatus());
	}

	@Test
	public void testApplicationStateUnknown() {
		Collection<GWTApplication> applications = new ArrayList<GWTApplication>();
		applications.add(getApplication("TestApp1"));
		applications.add(getApplication("TestApp2"));
		
		Map<String,List<GWTLocationSpecificStatus>> appStatuses = new HashMap<String,List<GWTLocationSpecificStatus>>();
		appStatuses.put("TestApp1", getUpStatusList());
		appStatuses.put("TestApp1", getUpStatusList());
		ApplicationState appState = new ApplicationState(m_from, m_to, applications, appStatuses);
		
		assertEquals(Status.UNKNOWN, appState.getStatus());
	}

	@Test
	public void testApplicationStateMarginal() {
		Collection<GWTApplication> applications = new ArrayList<GWTApplication>();
		applications.add(getApplication("TestApp1"));
		applications.add(getApplication("TestApp2"));
		
		Map<String,List<GWTLocationSpecificStatus>> appStatuses = new HashMap<String,List<GWTLocationSpecificStatus>>();
		appStatuses.put("TestApp1", getMarginalStatusList());
		appStatuses.put("TestApp2", getUpStatusList());
		ApplicationState appState = new ApplicationState(m_from, m_to, applications, appStatuses);
		
		assertEquals(Status.MARGINAL, appState.getStatus());
	}

	@Test
	public void testApplicationStateDown() {
		Collection<GWTApplication> applications = new ArrayList<GWTApplication>();
		applications.add(getApplication("TestApp1"));
		applications.add(getApplication("TestApp2"));
		
		Map<String,List<GWTLocationSpecificStatus>> appStatuses = new HashMap<String,List<GWTLocationSpecificStatus>>();
		appStatuses.put("TestApp1", getDownStatusList());
		appStatuses.put("TestApp2", getUpStatusList());
		ApplicationState appState = new ApplicationState(m_from, m_to, applications, appStatuses);
		
		assertEquals(Status.DOWN, appState.getStatus());
	}
	
	private GWTApplicationStatus getDownApplicationStatus(final String appName) {
		List<GWTLocationSpecificStatus> statuses = getDownStatusList();
		
		GWTApplicationStatus status = new GWTApplicationStatus(getApplication(appName), m_from, m_to, statuses);
		return status;
	}

	private List<GWTLocationSpecificStatus> getDownStatusList() {
		List<GWTLocationSpecificStatus> statuses = new ArrayList<GWTLocationSpecificStatus>();

		int offset = monitorOffset;
		
		// identical overlaps
		Date date = m_from;
		statuses.add(getUpStatus(date, "RDU", offset + 1));
		statuses.add(getUpStatus(date, "RDU", offset + 2));
		statuses.add(getUpStatus(date, "RDU", offset + 3));

		date = new Date(date.getTime() + 1000);
		statuses.add(getDownStatus(date, "RDU", offset + 1));
		date = new Date(date.getTime() + 1000);
		statuses.add(getDownStatus(date, "RDU", offset + 2));
		date = new Date(date.getTime() + 1000);
		statuses.add(getDownStatus(date, "RDU", offset + 3));
		
		date = new Date(date.getTime() + FIVE_MINUTES);
		statuses.add(getDownStatus(date, "RDU", offset + 1));
		date = new Date(date.getTime() + 1000);
		statuses.add(getDownStatus(date, "RDU", offset + 2));
		date = new Date(date.getTime() + 1000);
		statuses.add(getDownStatus(date, "RDU", offset + 3));
		
		monitorOffset += offset;
		return statuses;
	}

	private GWTApplicationStatus getUpApplicationStatus(final String appName) {
		List<GWTLocationSpecificStatus> statuses = getUpStatusList();
		GWTApplicationStatus status = new GWTApplicationStatus(getApplication(appName), m_from, m_to, statuses);
		return status;
	}

	private List<GWTLocationSpecificStatus> getUpStatusList() {
		List<GWTLocationSpecificStatus> statuses = new ArrayList<GWTLocationSpecificStatus>();
		Date date = m_from;
		for (int i = 0; i < 5; i++) {
			statuses.add(getStatus(date, "RDU", monitorOffset + 1, up(date)));
			date = new Date(date.getTime() + FIVE_MINUTES);
		}
		monitorOffset += 1;
		return statuses;
	}

	private GWTApplicationStatus getMarginalApplicationStatus(final String appName) {
		List<GWTLocationSpecificStatus> statuses = getMarginalStatusList();
		GWTApplicationStatus status = new GWTApplicationStatus(getApplication(appName), m_from, m_to, statuses);
		return status;
	}

	private List<GWTLocationSpecificStatus> getMarginalStatusList() {
		List<GWTLocationSpecificStatus> statuses = new ArrayList<GWTLocationSpecificStatus>();
		Date date = m_from;
		for (int i = 0; i < 6; i++) {
			statuses.add(getStatus(date, "RDU", monitorOffset + 1, up(date)));
			statuses.add(getStatus(date, "RDU", monitorOffset + 2, down(date, "I'm so high, I have no idea what's going on!")));
			date = new Date(date.getTime() + FIVE_MINUTES);
		}
		statuses.add(getStatus(date, "RDU", monitorOffset + 1, up(date)));
		statuses.add(getStatus(date, "RDU", monitorOffset + 2, up(date)));
		
		monitorOffset += 2;
		return statuses;
	}

	private GWTLocationSpecificStatus getUpStatus(Date date, String monitorName, Integer monitorId) {
		return getStatus(date, monitorName, monitorId, up(date));
	}
	
	private GWTLocationSpecificStatus getDownStatus(Date date, String monitorName, Integer monitorId) {
		return getStatus(date, monitorName, monitorId, down(date, "Stuff be broke, yo!"));
	}

	private GWTPollResult up(Date date) {
		return new GWTPollResult("Up", date, "Everything is A-OK", m_random.nextDouble() * 300);
	}

	private GWTPollResult down(Date date, String reason) {
		return new GWTPollResult("Down", date, reason, null);
	}

	private GWTApplication getApplication(String name) {
		GWTApplication app = m_applications.get(name);
		if (app == null) {
			app = new GWTApplication();
			app.setId(count++);
			app.setName(name);
			Set<GWTMonitoredService> services = new HashSet<GWTMonitoredService>();
			services.add(getMonitoredService());
			app.setServices(services);
			m_applications.put(name, app);
		}
		return app;
	}
	private GWTLocationSpecificStatus getStatus(Date date, String monitorName, int monitorId, GWTPollResult result) {
		GWTLocationSpecificStatus status = new GWTLocationSpecificStatus();
		status.setId(count++);
		status.setLocationMonitor(getLocationMonitor(date, monitorName, monitorId, "STARTED"));
		status.setMonitoredService(getMonitoredService());
		status.setPollResult(result);
		status.setPollTime(date);
		return status;
	}

	private GWTMonitoredService getMonitoredService() {
		return getMonitoredService("HTTP");
	}

	private GWTMonitoredService getMonitoredService(String serviceName) {
		GWTMonitoredService service = m_services.get(serviceName);
		if (service == null) {
			service = new GWTMonitoredService();
			service.setId(count++);
			service.setApplications(Arrays.asList(new String[] { "TestApp1", "TestApp3"}));
			service.setHostname("localhost");
			service.setIfIndex(count++);
			service.setIpAddress("127.0.0.1");
			service.setIpInterfaceId(count++);
			service.setNodeId(count++);
			service.setServiceName(serviceName);
			m_services.put(serviceName, service);
		}
		return service;
	}

	private GWTLocationMonitor getLocationMonitor(Date checkinTime, String name, int id, String status) {
		final String monitorName = name + "-" + id;
		GWTLocationMonitor monitor = m_monitors.get(monitorName);
		if (monitor == null) {
			monitor = new GWTLocationMonitor();
			monitor.setId(id);
			monitor.setDefinitionName(name);
			monitor.setLastCheckInTime(checkinTime);
			monitor.setName(monitorName);
			monitor.setStatus(status);
			m_monitors.put(monitorName, monitor);
		} else {
			monitor.setLastCheckInTime(checkinTime);
			monitor.setStatus(status);
		}
		return monitor;
	}
}
