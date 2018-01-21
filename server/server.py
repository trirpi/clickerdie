import socket
import pyautogui
import pyqrcode

def main():
    port = 0 # when 0 will use free port
    ip = get_ip() # uses ip that routes to 8.8.8.8

    # start server
    s_server = start_socket_server(ip, port)
    
    # print qr code
    print(create_qr_code(s_server.getsockname()))

    try: 
        process_client(s_server)
    except KeyboardInterrupt:
        print('[*] Info: exiting')
    except:
        print('[*] Error: unexpected error occured')
    finally:
        s_server.close()


def start_socket_server(host, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((host, port))

    host, port = s.getsockname()

    print('[*] Info: Starting server on {}:{}'.format(host, port))
    s.listen(1) # accept up to 1 clients

    return s

def create_qr_code(sock_name):
    host, port = sock_name
    url = pyqrcode.create(host + ':' + str(port), error='L')
    return url.terminal(quiet_zone=1)

def process_client(socket_server):
    conn, addr = socket_server.accept()
    print('[*] Info: got connection from', addr)
    
    while True:
        message = conn.recv(1024).decode('utf-8')
        if message:
            print('[*] Info: received message:', message)

            if message.lower() == 'next':
                pyautogui.press('right')
            if message.lower() == 'previous':
                pyautogui.press('left')
        if not message:
            break
    conn.close()
    print('[*] Info: connection closed by client')

def get_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(("8.8.8.8", 80))
    ip = s.getsockname()[0]
    s.close()

    return ip

if __name__ == '__main__':
    main()

