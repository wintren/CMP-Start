package com.template.data.weather.mapper

/**
 * Captured verbatim from the live endpoint, trimmed only of the `*_units` blocks the parser
 * ignores — the shape, the nulls and the code values are as served.
 */
internal const val FORECAST_JSON = """
{
  "latitude": 52.52,
  "longitude": 13.419998,
  "generationtime_ms": 0.34,
  "utc_offset_seconds": 7200,
  "timezone": "Europe/Berlin",
  "elevation": 38.0,
  "current": {
    "time": "2026-09-09T12:30",
    "interval": 900,
    "temperature_2m": 19.5,
    "weather_code": 3,
    "wind_speed_10m": 4.0
  },
  "daily": {
    "time": ["2026-09-09","2026-09-10","2026-09-11","2026-09-12","2026-09-13","2026-09-14","2026-09-15"],
    "weather_code": [3, 61, 3, 3, 61, 95, 3],
    "temperature_2m_max": [21.5, 19.5, 21.0, 21.8, 19.6, 23.6, 27.8],
    "temperature_2m_min": [12.1, 11.4, 10.9, 12.6, 13.0, 14.2, 15.1],
    "precipitation_sum": [0.0, 4.3, 0.1, 0.0, 6.7, 11.2, 0.0],
    "precipitation_probability_max": [3, 71, 13, 6, 84, 92, 5],
    "wind_speed_10m_max": [4.2, 6.1, 3.8, 4.4, 7.9, 9.3, 3.1]
  }
}
"""

/** The same endpoint at the edge of its range: short arrays and nulls where data runs out. */
internal const val RAGGED_FORECAST_JSON = """
{
  "current": { "temperature_2m": 8.0, "weather_code": 45, "wind_speed_10m": 1.5 },
  "daily": {
    "time": ["2026-09-09","2026-09-10","2026-09-11"],
    "weather_code": [0, null],
    "temperature_2m_max": [18.0, 17.0, null],
    "temperature_2m_min": [9.0, null, 8.0],
    "precipitation_sum": [0.0],
    "precipitation_probability_max": [],
    "wind_speed_10m_max": [2.0]
  }
}
"""
