package com.metacontent.cobblenav.networking;

import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.networking.client.*;
import com.metacontent.cobblenav.networking.server.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class CobblenavPackets {
    public static final Identifier SPAWN_MAP_REQUEST_PACKET = new Identifier(Cobblenav.ID, "spawn_map_request_packet");
    public static final Identifier SPAWN_MAP_PACKET = new Identifier(Cobblenav.ID, "spawn_map_packet");
    public static final Identifier CONTACT_DATA_REQUEST_PACKET = new Identifier(Cobblenav.ID, "contact_data_request_packet");
    public static final Identifier CONTACT_DATA_PACKET = new Identifier(Cobblenav.ID, "contact_data_packet");
    public static final Identifier FOUND_POKEMON_REQUEST_PACKET = new Identifier(Cobblenav.ID, "found_pokemon_request_packet");
    public static final Identifier FOUND_POKEMON_PACKET = new Identifier(Cobblenav.ID, "found_pokemon_packet");
    public static final Identifier SAVE_FOUND_POKEMON_PACKET = new Identifier(Cobblenav.ID, "save_found_pokemon_packet");
    public static final Identifier RENDERABLE_POKEMON_REQUEST_PACKET = new Identifier(Cobblenav.ID, "renderable_pokemon_request_packet");
    public static final Identifier RENDERABLE_POKEMON_PACKET = new Identifier(Cobblenav.ID, "renderable_pokemon_packet");
    public static final Identifier REMOVE_LAST_FOUND_POKEMON_PACKET = new Identifier(Cobblenav.ID, "remove_last_found_pokemon_packet");
    public static final Identifier DELETE_CONTACT_PACKET = new Identifier(Cobblenav.ID, "delete_contact_packet");
    public static final Identifier TRACKED_ENTITY_ID_REQUEST_PACKET = new Identifier(Cobblenav.ID, "tracked_entity_id_request_packet");
    public static final Identifier TRACKED_ENTITY_ID_PACKET = new Identifier(Cobblenav.ID, "tracked_entity_id_packet");
    public static final Identifier SAVE_PREFERENCES_PACKET = new Identifier(Cobblenav.ID, "save_preferences_packet");
    public static final Identifier SAVED_PREFERENCES_REQUEST_PACKET = new Identifier(Cobblenav.ID, "saved_preferences_request_packet");
    public static final Identifier SAVED_PREFERENCES_PACKET = new Identifier(Cobblenav.ID, "saved_preferences_packet");
    public static final Identifier STREAK_REQUEST_PACKET = new Identifier(Cobblenav.ID, "streak_request_packet");
    public static final Identifier STREAK_PACKET = new Identifier(Cobblenav.ID, "streak_packet");
    public static final Identifier PLAYER_STATS_REQUEST_PACKET = new Identifier(Cobblenav.ID, "player_stats_request_packet");
    public static final Identifier PLAYER_STATS_PACKET = new Identifier(Cobblenav.ID, "player_stats_packet");
    public static final Identifier ALL_POKEMON_PACKET = new Identifier(Cobblenav.ID, "all_pokemon_packet");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SPAWN_MAP_REQUEST_PACKET, SpawnMapRequestReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(CONTACT_DATA_REQUEST_PACKET, ContactDataRequestReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(FOUND_POKEMON_REQUEST_PACKET, FoundPokemonRequestReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(SAVE_FOUND_POKEMON_PACKET, SaveFoundPokemonReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(RENDERABLE_POKEMON_REQUEST_PACKET, RenderablePokemonRequestReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(REMOVE_LAST_FOUND_POKEMON_PACKET, RemoveLastFoundPokemonReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(DELETE_CONTACT_PACKET, DeleteContactReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(TRACKED_ENTITY_ID_REQUEST_PACKET, TrackedEntityIdRequestReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(SAVED_PREFERENCES_REQUEST_PACKET, SavedPreferencesRequestReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(SAVE_PREFERENCES_PACKET, SavePreferencesReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(STREAK_REQUEST_PACKET, StreakRequestReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(PLAYER_STATS_REQUEST_PACKET, PlayerStatsRequestReceiver::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SPAWN_MAP_PACKET, SpawnMapReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(CONTACT_DATA_PACKET, ContactDataReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(FOUND_POKEMON_PACKET, FoundPokemonReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(RENDERABLE_POKEMON_PACKET, RenderablePokemonReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(TRACKED_ENTITY_ID_PACKET, TrackedEntityIdReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(SAVED_PREFERENCES_PACKET, SavedPreferencesReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(STREAK_PACKET, StreakReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(PLAYER_STATS_PACKET, PlayerStatsReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(ALL_POKEMON_PACKET, AllPokemonReceiver::receive);
    }
}
