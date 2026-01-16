# Hytale Idle Timeout
A simple idle timeout plugin for hytale that will kick players if they don't move for a configurable amount of time.

## Configuration
You have to configure the idle timeout duration in the server's `config.json` file:
```json5
{
  // ...
  "Modules": {
    "IdleTimeout": {
      "TimeoutMinutes": 30
    }
  },
  // ...
}
```
